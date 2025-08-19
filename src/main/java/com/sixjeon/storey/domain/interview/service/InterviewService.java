package com.sixjeon.storey.domain.interview.service;

import com.sixjeon.storey.domain.interview.util.AiGateWay;
import com.sixjeon.storey.domain.interview.util.S3Uploader;
import com.sixjeon.storey.domain.interview.web.dto.CharacterRes;
import com.sixjeon.storey.domain.interview.web.dto.CreateQuestionReq;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;
import com.sixjeon.storey.domain.interview.web.dto.InterviewRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final AiGateWay aiGateWay;
    private final S3Uploader s3Uploader;

    // 첫 질문
    public InterviewRes startInterview(CreateQuestionReq req) {
        String q1 = aiGateWay.firstQuestion(req.getStoreMood(), req.getBusinessType());
        return new InterviewRes(q1);
    }

    // 다음 질문
    public InterviewRes processInterview(InterviewReq req) {
        String next = aiGateWay.nextQuestion(req.getAnswer());
        return new InterviewRes(next);
    }

    // 캐릭터 생성 (요약 + 이미지 → S3)
    public CharacterRes generateCharacter(@Valid InterviewReq interviewReq) {
        String oneLine = aiGateWay.oneLineSummary(interviewReq.getAnswer());
        String imgPrompt = aiGateWay.buildCharacterImagePrompt(oneLine);
        byte[] png = aiGateWay.generateImagePng(imgPrompt, "1024x1024");

        String key = "characters/%s.png".formatted(java.util.UUID.randomUUID());
        String url = s3Uploader.uploadPngAndGetUrl(png, key);

        return new CharacterRes(url, oneLine); // CharacterRes 구조에 맞게 생성자/빌더 사용
    }
}
