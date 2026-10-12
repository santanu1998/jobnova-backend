package com.globalco.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gemini.api")
public class GeminiProperties {
    private String key;
    private String model;
    private int maxOutputTokens = 2048;
    private double temperature = 0.7;
    /** How many times to retry when Gemini is overloaded or rate limiting us. */
    private int maxRetries = 3;
    /** Base backoff in milliseconds; doubles on each retry. */
    private long retryDelayMs = 1000;
}