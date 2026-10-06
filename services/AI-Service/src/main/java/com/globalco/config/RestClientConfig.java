package com.globalco.config;

import com.google.genai.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

    @Bean
    public Client genAiClient(GeminiProperties props) {
        validateKey(props.getKey());
        return Client.builder()
                .apiKey(props.getKey())
                .build();
    }

    /**
     * Fail fast on a missing key.
     * <p>
     * Spring leaves an unresolved placeholder in {@code @ConfigurationProperties} as the
     * literal text "${GEMINI_API_KEY}". Without this check the service starts happily,
     * sends that literal string to Google as the API key and every AI call fails at
     * runtime with "400 API key not valid" - which looks like a bad key rather than a
     * missing environment variable. Crashing here makes the real cause obvious.
     */
    private void validateKey(String key) {
        if (key == null || key.isBlank() || key.contains("${")) {
            throw new IllegalStateException("""
                    GEMINI_API_KEY is not set, so AI-Service cannot reach the Gemini API.

                    Set it in the environment of this service:
                      - locally : add GEMINI_API_KEY=<your key> to the project .env file
                      - docker  : docker-compose.yml -> ai-service -> environment:
                                    GEMINI_API_KEY: ${GEMINI_API_KEY}
                                  (the value is read from the .env file passed via --env-file)
                      - CI/CD   : GitHub repository secret GEMINI_API_KEY

                    Get a key from Google AI Studio: https://aistudio.google.com/apikey
                    """);
        }
    }
}
