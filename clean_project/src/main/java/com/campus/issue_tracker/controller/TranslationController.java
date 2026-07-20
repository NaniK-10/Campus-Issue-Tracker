package com.campus.issue_tracker.controller;

import com.campus.issue_tracker.service.TranslationApiService;
import com.campus.issue_tracker.service.TranslationSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Admin endpoint to trigger auto-translation sync.
 * Uses Google Cloud Translation API to translate English messages to Sinhala and Tamil.
 */
@RestController
@RequestMapping("/api/admin")
public class TranslationController {

    private final TranslationSyncService translationSyncService;
    private final TranslationApiService translationApiService;

    public TranslationController(TranslationSyncService translationSyncService,
                                TranslationApiService translationApiService) {
        this.translationSyncService = translationSyncService;
        this.translationApiService = translationApiService;
    }

    @PostMapping("/translate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> syncTranslations() {
        var result = translationSyncService.syncAll();
        return ResponseEntity.ok(Map.of(
                "success", result.success(),
                "message", result.message(),
                "translatedCount", result.translatedCount()
        ));
    }

    @GetMapping("/translate/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(Map.of(
                "apiAvailable", translationApiService.isAvailable()
        ));
    }
}
