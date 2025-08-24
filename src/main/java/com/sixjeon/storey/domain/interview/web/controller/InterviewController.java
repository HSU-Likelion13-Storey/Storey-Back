package com.sixjeon.storey.domain.interview.web.controller;

import com.sixjeon.storey.domain.interview.service.InterviewService;
import com.sixjeon.storey.domain.character.web.dto.CharacterRes;
import com.sixjeon.storey.domain.interview.web.dto.CreateQuestionReq;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;
import com.sixjeon.storey.domain.interview.web.dto.InterviewRes;
import com.sixjeon.storey.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    // 첫번째 질문 생성 (가게 업종, 분위기에 맞게)
    @PostMapping("/interview/create")
    public ResponseEntity<SuccessResponse<?>> startInterview(@RequestBody @Valid CreateQuestionReq createQuestionReq) {
        InterviewRes interviewRes = interviewService.startInterview(createQuestionReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(interviewRes));
    }

    @PostMapping("/interview")
    public ResponseEntity<SuccessResponse<?>> interview(@RequestBody @Valid InterviewReq interviewReq) {
        InterviewRes interviewRes = interviewService.processInterview(interviewReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(interviewRes));
    }

}
