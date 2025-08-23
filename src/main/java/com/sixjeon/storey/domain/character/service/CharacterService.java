package com.sixjeon.storey.domain.character.service;

import com.sixjeon.storey.domain.character.web.dto.CharacterDetailRes;
import com.sixjeon.storey.domain.character.web.dto.CharacterRes;
import com.sixjeon.storey.domain.character.web.dto.UpdateCharacterReq;
import com.sixjeon.storey.domain.character.web.dto.UpdateCharacterRes;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;

public interface CharacterService {
    // 캐릭터 생성 (요약 + 이미지 → S3)
    CharacterRes generateCharacter(InterviewReq interviewReq, String ownerLoginId);
    // 캐릭터 재생성 로직 추가
    CharacterRes regenerateCharacter(InterviewReq interviewReq, String ownerLoginId);
    // 캐릭터 상세 조회
    CharacterDetailRes getCharacterDetail(Long characterId);
    // 캐릭터 정보 수정
    UpdateCharacterRes updateCharacter(Long characterId, UpdateCharacterReq characterUpdateReq, String ownerLoginId);
}
