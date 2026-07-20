package com.campus.issue_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public String save(MultipartFile file) {
        try {
            // Create uploads folder if it doesn't exist
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }
            // Generate a unique name for the file to avoid overwriting
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(filename));
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}
