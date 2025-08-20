package com.sixjeon.storey.domain.interview.service;

import com.sixjeon.storey.domain.interview.util.AiGateWay;
import com.sixjeon.storey.domain.interview.util.S3Uploader;
import com.sixjeon.storey.domain.character.web.dto.CharacterRes;
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


}
