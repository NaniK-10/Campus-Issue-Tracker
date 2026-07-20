package com.campus.issue_tracker.repository;

import com.campus.issue_tracker.entity.Issue;
import com.campus.issue_tracker.entity.IssueCategory;
import com.campus.issue_tracker.entity.IssueStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IssueSpecification {

    public static Specification<Issue> filterIssues(String query, IssueCategory category, IssueStatus status,
            String dateRange) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Search Query (Title, Description, Location)
            if (query != null && !query.trim().isEmpty()) {
                String likePattern = "%" + query.trim().toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern);
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                        likePattern);
                Predicate locationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("location")),
                        likePattern);
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate, locationPredicate));
            }

            // 2. Category Filter
            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            // 3. Status Filter
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 4. Date Filter
            if (dateRange != null && !dateRange.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startDate = null;

                switch (dateRange) {
                    case "today":
                        startDate = now.toLocalDate().atStartOfDay();
                        break;
                    case "7days":
                        startDate = now.minusDays(7);
                        break;
                    case "30days":
                        startDate = now.minusDays(30);
                        break;
                    default:
                        // "ALL" or unknown -> do nothing
                        break;
                }

                if (startDate != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
