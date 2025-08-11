package com.sixjeon.storey.domain.interview.web.controller;

import com.sixjeon.storey.domain.interview.service.InterviewService;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;
import com.sixjeon.storey.domain.interview.web.dto.InterviewRes;
import com.sixjeon.storey.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/interview")
    public ResponseEntity<SuccessResponse<?>> interview(@RequestBody @Valid InterviewReq interviewReq) {
        InterviewRes interviewRes = interviewService.processInterview(interviewReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(interviewRes));
    }
}
