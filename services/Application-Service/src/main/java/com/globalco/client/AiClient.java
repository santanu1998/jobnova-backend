package com.globalco.client;

import com.globalco.payload.ScreeningScoreRequest;
import com.globalco.payload.ScreeningScoreResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AI-SERVICE")
public interface AiClient {

    @PostMapping("/api/ai/application/screening-core")
    ScreeningScoreResult scoreCandidate(@RequestBody ScreeningScoreRequest request);
}
