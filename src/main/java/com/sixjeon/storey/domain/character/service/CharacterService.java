package com.sixjeon.storey.domain.character.service;

import com.sixjeon.storey.domain.character.web.dto.CharacterRes;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;

public interface CharacterService {
    // 캐릭터 생성 (요약 + 이미지 → S3)
    CharacterRes generateCharacter(InterviewReq interviewReq, String ownerLoginId);
}
