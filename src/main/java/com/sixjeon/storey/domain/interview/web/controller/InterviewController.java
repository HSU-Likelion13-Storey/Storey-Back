package com.sixjeon.storey.domain.interview.web.controller;

import com.sixjeon.storey.domain.interview.service.InterviewService;
import com.sixjeon.storey.domain.interview.web.dto.CharacterRes;
import com.sixjeon.storey.domain.interview.web.dto.CreateQuestionReq;
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

    // 캐릭터 생성 & 인터뷰 한줄 요약
    /**@PostMapping("/character")
    public ResponseEntity<SuccessResponse<?>> character(@RequestBody @Valid InterviewReq interviewReq) {
        CharacterRes characterRes = interviewService.generateCharacter(interviewReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(characterRes));
    }
    **/
}
