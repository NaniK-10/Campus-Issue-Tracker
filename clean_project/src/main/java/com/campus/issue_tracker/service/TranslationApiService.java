package com.campus.issue_tracker.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that calls Google Cloud Translation API to translate text.
 * Free tier: 500,000 characters/month.
 * Get API key: https://console.cloud.google.com/apis/credentials
 */
@Service
public class TranslationApiService {

    private static final String GOOGLE_TRANSLATE_URL = "https://translation.googleapis.com/language/translate/v2";

    @Value("${translation.api.key:}")
    private String apiKey;

    @Value("${translation.api.enabled:false}")
    private boolean enabled;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean isAvailable() {
        return enabled && apiKey != null && !apiKey.isBlank();
    }

    /**
     * Translate text from English to the target language.
     * @param text Text to translate
     * @param targetLanguage ISO 639-1 code (e.g. "si" for Sinhala, "ta" for Tamil)
     * @return Translated text, or original if API unavailable
     */
    public String translate(String text, String targetLanguage) {
        if (!isAvailable() || text == null || text.isBlank()) {
            return text;
        }
        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = String.format("%s?key=%s&q=%s&target=%s&source=en", GOOGLE_TRANSLATE_URL, apiKey, encodedText, targetLanguage);

            ResponseEntity<GoogleTranslateResponse> response = restTemplate.getForEntity(url, GoogleTranslateResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                var data = response.getBody().data;
                if (data != null && data.translations != null && !data.translations.isEmpty()) {
                    return data.translations.get(0).translatedText;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Translation API error: " + e.getMessage());
        }
        return text;
    }

    /**
     * Translate multiple texts in one batch (up to 100 per request for efficiency).
     */
    public List<String> translateBatch(List<String> texts, String targetLanguage) {
        if (!isAvailable() || texts == null || texts.isEmpty()) {
            return texts;
        }
        return texts.stream()
                .map(text -> translate(text, targetLanguage))
                .collect(Collectors.toList());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GoogleTranslateResponse {
        @JsonProperty("data")
        Data data;

        @JsonIgnoreProperties(ignoreUnknown = true)
        static class Data {
            @JsonProperty("translations")
            List<Translation> translations;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Translation {
        @JsonProperty("translatedText")
        private String translatedText;
    }
}
