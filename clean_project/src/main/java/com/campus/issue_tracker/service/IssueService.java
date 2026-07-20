package com.campus.issue_tracker.service;

import com.campus.issue_tracker.dto.IssueRequest;
import com.campus.issue_tracker.entity.*;
import com.campus.issue_tracker.repository.IssueRepository;
import com.campus.issue_tracker.repository.IssueSpecification;
import com.campus.issue_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public Issue createIssue(IssueRequest request, String username) {

        User reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Issue issue = new Issue();
        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());

        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            issue.setLocation(request.getLocation());
        } else {
            if (request.getCategory() == IssueCategory.ACADEMIC) {
                issue.setLocation("Academic Issue: " + request.getCourseUnit());
            } else if (request.getCategory() == IssueCategory.HOSTEL) {
                issue.setLocation("Hostel: " + request.getHostelBlock() + ", Room: " + request.getRoomNumber());
            } else if (request.getCategory() == IssueCategory.PAYMENTS) {
                issue.setLocation("Payment Issue: " + request.getPaymentId());
            } else {
                issue.setLocation("Not Specified");
            }
        }

        issue.setLatitude(request.getLatitude());
        issue.setLongitude(request.getLongitude());
        issue.setReporter(reporter);
        issue.setStatus(IssueStatus.PENDING);

        issue.setAnonymous(request.isAnonymous());
        issue.setCategory(request.getCategory());

        issue.setCourseUnit(request.getCourseUnit());
        issue.setPaymentId(request.getPaymentId());
        issue.setHostelBlock(request.getHostelBlock());
        issue.setRoomNumber(request.getRoomNumber());

        issue.setEscalated(false);
        issue.setEscalationLevel(0);
        issue.setEscalationTime(LocalDateTime.now());

        Issue savedIssue = issueRepository.save(issue);

        auditLogService.logEvent(
                "ISSUE_CREATED",
                savedIssue.getId(),
                "Issue",
                username,
                "Created issue: " + savedIssue.getTitle());

        return savedIssue;
    }

    public Issue saveDirectly(Issue issue) {
        return issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Issue updateStatus(Long id, IssueStatus newStatus) {

        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        IssueStatus oldStatus = issue.getStatus();
        issue.setStatus(newStatus);

        issue.setEscalated(false);
        issue.setEscalationLevel(0);
        issue.setEscalationTime(LocalDateTime.now());

        Issue savedIssue = issueRepository.save(issue);

        String username = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();

        auditLogService.logEvent(
                "STATUS_UPDATED",
                savedIssue.getId(),
                "Issue",
                username,
                "Changed status from " + oldStatus + " to " + newStatus);

        return savedIssue;
    }

    public Issue addStudentFeedback(Long id, String feedback, Integer rating, String username) {

        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        if (!issue.getReporter().getUsername().equals(username)) {
            throw new RuntimeException("You can only provide feedback for your own issue");
        }

        issue.setStudentFeedback(feedback);
        issue.setRating(rating);

        Issue savedIssue = issueRepository.save(issue);

        auditLogService.logEvent(
                "FEEDBACK_ADDED",
                savedIssue.getId(),
                "Issue",
                username,
                "Added feedback. Rating: " + rating);

        return savedIssue;
    }

    public Page<Issue> getIssuesPaged(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return issueRepository.findAll(pageable);
    }

    public long countByStatus(IssueStatus status) {
        return issueRepository.countByStatus(status);
    }

    public long countAll() {
        return issueRepository.count();
    }

    public Page<Issue> getIssuesWithFilters(int page, int size, String sortBy, String direction,
            String query, IssueCategory category, IssueStatus status, String dateRange) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Issue> spec = IssueSpecification.filterIssues(query, category, status, dateRange);

        return issueRepository.findAll(spec, pageable);
    }

    public boolean isPotentialDuplicate(String title, IssueCategory category, String location) {
        return issueRepository.existsByTitleIgnoreCaseAndCategoryAndLocationIgnoreCase(title, category, location);
    }
}
