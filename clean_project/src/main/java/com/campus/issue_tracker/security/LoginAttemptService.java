package com.campus.issue_tracker.security;

import com.campus.issue_tracker.entity.User;
import com.campus.issue_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginAttemptService {

    private static final int MAX_FAILED_ATTEMPTS = 3;

    private final UserRepository userRepository;

    @Autowired
    public LoginAttemptService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateFailedAttempts(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> {
            int newFailedAttempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(newFailedAttempts);

            if (newFailedAttempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now());
            }

            userRepository.save(user);
        });
    }

    public void resetFailedAttempts(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> {
            user.setFailedAttempts(0);
            userRepository.save(user);
        });
    }
}