package com.globalco.client;

import com.globalco.dto.response.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOB-SERVICE")
public interface JobClient {

    @GetMapping("/api/jobs/{id}")
    JobResponse getJobById(
            @PathVariable Long id);
}