package com.sixjeon.storey.domain.character.web.controller;

import com.sixjeon.storey.domain.character.service.CharacterService;
import com.sixjeon.storey.domain.character.web.dto.CharacterDetailRes;
import com.sixjeon.storey.domain.character.web.dto.CharacterRes;
import com.sixjeon.storey.domain.character.web.dto.UpdateCharacterReq;
import com.sixjeon.storey.domain.character.web.dto.UpdateCharacterRes;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;
import com.sixjeon.storey.global.response.SuccessResponse;
import com.sixjeon.storey.global.security.details.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    // 캐릭터 생성 & 인터뷰 한줄 요약
    @PostMapping("/owner/character")
    public ResponseEntity<SuccessResponse<?>> character(@RequestBody @Valid InterviewReq interviewReq,
                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CharacterRes characterRes = characterService.generateCharacter(interviewReq, customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(characterRes));
    }

    @GetMapping("/characters/{characterId}")
    public ResponseEntity<SuccessResponse<?>> characterDetail(@PathVariable Long characterId) {
        CharacterDetailRes characterRes = characterService.getCharacterDetail(characterId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(characterRes));

    }

    @PutMapping("/owner/characters/{characterId}")
    public ResponseEntity<SuccessResponse<?>> updateCharacter(@PathVariable Long characterId,
                                                              @RequestBody @Valid UpdateCharacterReq updateCharacterReq,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        UpdateCharacterRes updateCharacterRes = characterService.updateCharacter(characterId, updateCharacterReq, customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(updateCharacterRes));
    }
}
