package com.campus.issue_tracker.controller;

import com.campus.issue_tracker.dto.IssueRequest;
import com.campus.issue_tracker.entity.*;
import com.campus.issue_tracker.service.IssueService;
import com.campus.issue_tracker.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/with-image")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Issue> reportIssueWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam(value = "anonymous", defaultValue = "false") boolean anonymous,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,

            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication authentication) {

        IssueRequest request = new IssueRequest();
        request.setTitle(title.trim());
        request.setDescription(description);
        request.setLocation(location);
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setAnonymous(anonymous);

        Issue issue = issueService.createIssue(request, authentication.getName());

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.save(file);
            issue.setAttachmentPath(fileName);
            // Save again to update the path
            issue = issueService.saveDirectly(issue);
        }

        return ResponseEntity.ok(issue);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Issue> reportIssue(@RequestBody IssueRequest request, Authentication authentication) {
        return ResponseEntity.ok(issueService.createIssue(request, authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Issue> updateStatus(@PathVariable Long id,
            @RequestParam IssueStatus status) {
        return ResponseEntity.ok(issueService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/feedback")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Issue> addFeedback(@PathVariable Long id,
            @RequestParam(required = false) String feedback,
            @RequestParam(required = false) Integer rating,
            Authentication authentication) {
        return ResponseEntity.ok(issueService.addStudentFeedback(id, feedback, rating, authentication.getName()));
    }

    @GetMapping("/paged")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Page<Issue>> getIssuesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(issueService.getIssuesPaged(page, size, sortBy, direction));
    }

    @GetMapping("/check-duplicate")
    public ResponseEntity<Boolean> checkDuplicate(
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String location) {
        IssueCategory cat = IssueCategory.valueOf(category.toUpperCase());
        return ResponseEntity.ok(issueService.isPotentialDuplicate(title, cat, location));
    }
}
