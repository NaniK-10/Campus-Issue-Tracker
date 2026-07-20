package com.campus.issue_tracker.service;

import com.campus.issue_tracker.entity.Issue;
import com.campus.issue_tracker.entity.IssueStatus;
import com.campus.issue_tracker.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EscalationService {

    private final IssueRepository issueRepository;

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void checkEscalations() {

        LocalDateTime now = LocalDateTime.now();

        // 🔥 LEVEL 1 - Only get PENDING older than 6h
        List<Issue> pendingIssues =
                issueRepository.findByStatusAndCreatedAtBefore(
                        IssueStatus.PENDING,
                        now.minusHours(6)
                );

        for (Issue issue : pendingIssues) {

            if (issue.getEscalationLevel() < 1) {

                issue.setEscalated(true);
                issue.setEscalationLevel(1);
                issue.setStatus(IssueStatus.IN_PROGRESS);
                issue.setLastEscalatedAt(now);
            }
        }

        // 🔥 LEVEL 2 - Only get IN_PROGRESS older than 12h
        List<Issue> inProgressIssues =
                issueRepository.findByStatusAndUpdatedAtBefore(
                        IssueStatus.IN_PROGRESS,
                        now.minusHours(12)
                );

        for (Issue issue : inProgressIssues) {

            if (issue.getEscalationLevel() < 2) {

                issue.setEscalationLevel(2);
                issue.setLastEscalatedAt(now);
            }
        }

        issueRepository.saveAll(pendingIssues);
        issueRepository.saveAll(inProgressIssues);
    }
}
