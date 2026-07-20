package com.campus.issue_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CampusIssueAndTrackingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusIssueAndTrackingSystemApplication.class, args);
	}
}
