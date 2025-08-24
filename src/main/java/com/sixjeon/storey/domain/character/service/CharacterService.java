package com.sixjeon.storey.domain.character.service;

import com.sixjeon.storey.domain.character.web.dto.*;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;

public interface CharacterService {
    // 캐릭터 생성 (요약 + 이미지 → S3)
    CharacterRes generateCharacter(CharacterCreateReq characterCreateReq, String ownerLoginId);
    // 캐릭터 재생성 로직 추가
    CharacterRes regenerateCharacter(CharacterCreateReq characterCreateReq, String ownerLoginId);
    // 캐릭터 상세 조회
    CharacterDetailRes getCharacterDetail(Long characterId);
    // 캐릭터 정보 수정
    UpdateCharacterRes updateCharacter(UpdateCharacterReq characterUpdateReq, String ownerLoginId);
    // 사장님용 내 캐릭터 조회
    MyCharacterRes getMyCharacter(String ownerLoginId);
}
