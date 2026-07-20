package com.campus.issue_tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendIssueCreatedEmail(String toEmail, com.campus.issue_tracker.entity.Issue issue) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("eaahashara01@gmail.com"); // Consider moving to properties if possible, but keeping hardcoded
                                                   // as found
        message.setTo(toEmail);
        message.setSubject("Issue Created: " + issue.getTitle());

        StringBuilder body = new StringBuilder();
        body.append("Hello,\n\n");
        body.append("Your issue has been successfully reported.\n\n");
        body.append("Issue ID: ").append(issue.getId()).append("\n");
        body.append("Title: ").append(issue.getTitle()).append("\n");
        body.append("Category: ").append(issue.getCategory()).append("\n");
        body.append("Status: ").append(issue.getStatus()).append("\n");
        body.append("Location: ").append(issue.getLocation()).append("\n");
        body.append("Created Date: ").append(issue.getCreatedAt()).append("\n\n");
        body.append("We will review it shortly.");

        message.setText(body.toString());

        try {
            mailSender.send(message);
            System.out.println("Issue Created Email sent to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send Issue Created Email: " + e.getMessage());
        }
    }

    public void sendIssueStatusUpdatedEmail(String toEmail, com.campus.issue_tracker.entity.Issue issue,
            com.campus.issue_tracker.entity.IssueStatus oldStatus) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("eaahashara01@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Issue Status Updated: " + issue.getTitle());

        StringBuilder body = new StringBuilder();
        body.append("Hello,\n\n");
        body.append("The status of your issue has changed.\n\n");
        body.append("Issue Title: ").append(issue.getTitle()).append("\n");
        body.append("Old Status: ").append(oldStatus).append("\n");
        body.append("New Status: ").append(issue.getStatus()).append("\n");
        body.append("Updated Date: ").append(java.time.LocalDateTime.now()).append("\n\n");
        body.append("Thank you for using Campus Issue Tracker.");

        message.setText(body.toString());

        try {
            mailSender.send(message);
            System.out.println("Status Update Email sent to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send Status Update Email: " + e.getMessage());
        }
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("eaahashara01@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Generic email sent to " + to);
        } catch (Exception e) {
            System.err.println("Failed to send generic email: " + e.getMessage());
        }
    }
}
