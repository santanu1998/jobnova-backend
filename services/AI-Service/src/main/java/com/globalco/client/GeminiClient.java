package com.globalco.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.globalco.config.GeminiProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiClient.class);

    private final Client genaiClient;
    private final GeminiProperties geminiProperties;
    private final ObjectMapper objectMapper;


    public String generateText(String systemInstruction, String prompt) throws Exception {
        return generateText(
                systemInstruction,
                prompt,
                geminiProperties.getTemperature(),
                geminiProperties.getMaxOutputTokens()
        );
    }

    public <T> T generateJson(String systemInstruction, String prompt, Class<T> responseType) throws Exception {
        return callJson(systemInstruction, prompt, responseType);
    }


    public String generateText(String systemInstruction, String prompt, double temperature, int maxTokens) throws Exception {
        return callText(
                systemInstruction, prompt,
                (float) geminiProperties.getTemperature(),
                geminiProperties.getMaxOutputTokens()
        );
    }

    private <T> T callJson(String systemInstruction,
                           String prompt,
                           Class<T> responseType) throws Exception {
        GenerateContentConfig config = buildConfig(systemInstruction, 0.3f,
                geminiProperties.getMaxOutputTokens(), true);
        String json = callWithRetry(prompt, config);
        return objectMapper.readValue(json, responseType);
    }

    //    system_prompt = system_instruction
    private String callText(String systemInstruction,
                            String prompt,
                            float temperature,
                            int maxTokens) throws Exception {
        GenerateContentConfig config = buildConfig(systemInstruction, temperature, maxTokens, false);
        return callWithRetry(prompt, config);
    }

    /**
     * Calls Gemini, retrying transient failures with exponential backoff.
     * <p>
     * Gemini regularly answers 503 UNAVAILABLE ("model is overloaded") or
     * 429 RESOURCE_EXHAUSTED under load. Without this, a single blip surfaces to the
     * user as "internal server error". Permanent failures (bad key, unknown model)
     * are not retried - they would fail identically every time.
     */
    private String callWithRetry(String prompt, GenerateContentConfig config) throws Exception {
        int attempts = Math.max(1, geminiProperties.getMaxRetries());
        Exception last = null;

        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                GenerateContentResponse response = genaiClient.models.generateContent(
                        geminiProperties.getModel(), prompt, config);
                String text = response.text();
                if (text == null || text.isBlank()) {
                    throw new IllegalStateException("Gemini returned an empty response");
                }
                return text;
            } catch (Exception e) {
                last = e;
                if (!isRetryable(e) || attempt == attempts) {
                    break;
                }
                long backoff = geminiProperties.getRetryDelayMs() * (1L << (attempt - 1));
                log.warn("Gemini call failed (attempt {}/{}), retrying in {}ms: {}",
                        attempt, attempts, backoff, e.getMessage());
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        throw new Exception("failed to get response from gemini:"
                + (last == null ? "unknown error" : last.getMessage()), last);
    }

    /** Overload / rate-limit / transient network problems are worth another try. */
    private boolean isRetryable(Exception e) {
        String m = e.getMessage() == null ? "" : e.getMessage().toUpperCase();
        return m.contains("UNAVAILABLE") || m.contains("503")
                || m.contains("RESOURCE_EXHAUSTED") || m.contains("429")
                || m.contains("OVERLOADED") || m.contains("HIGH DEMAND")
                || m.contains("INTERNAL") || m.contains("500")
                || m.contains("DEADLINE") || m.contains("TIMEOUT")
                || m.contains("EMPTY RESPONSE");
    }

    private GenerateContentConfig buildConfig(String systemInstruction,
                                              float temperature, int maxTokens, boolean jsonMode) {
        GenerateContentConfig.Builder builder = GenerateContentConfig.builder()
                .temperature((float) temperature).maxOutputTokens(maxTokens);

        if (systemInstruction != null && !systemInstruction.isBlank()) {
            builder.systemInstruction(
                    Content.fromParts(Part.fromText(systemInstruction))
            );
        }
        if (jsonMode) {
            builder.responseMimeType("application/json");
        }
        return builder.build();
    }
}