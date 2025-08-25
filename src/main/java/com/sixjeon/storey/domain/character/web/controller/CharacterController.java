package com.sixjeon.storey.domain.character.web.controller;

import com.sixjeon.storey.domain.character.service.CharacterService;
import com.sixjeon.storey.domain.character.web.dto.*;
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
    public ResponseEntity<SuccessResponse<?>> character(@RequestBody @Valid CharacterCreateReq characterCreateReq,
                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CharacterRes characterRes = characterService.generateCharacter(characterCreateReq, customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(characterRes));
    }

    @PutMapping("/owner/character")
    public ResponseEntity<SuccessResponse<?>> regenerateCharacter(@RequestBody @Valid CharacterCreateReq characterCreateReq,
                                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CharacterRes characterRes = characterService.regenerateCharacter(characterCreateReq, customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(characterRes));
    }

    @GetMapping("/user/characters/{characterId}")
    public ResponseEntity<SuccessResponse<?>> characterDetail(@PathVariable Long characterId) {
        CharacterDetailRes characterRes = characterService.getCharacterDetail(characterId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(characterRes));

    }

    @PutMapping("/owner/character/update")
    public ResponseEntity<SuccessResponse<?>> updateCharacter(@RequestBody @Valid UpdateCharacterReq updateCharacterReq,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        UpdateCharacterRes updateCharacterRes = characterService.updateCharacter(updateCharacterReq, customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(updateCharacterRes));
    }

    // 사장님 캐릭터 조회
    @GetMapping("/owner/character")
    public ResponseEntity<SuccessResponse<?>> getMyCharacter(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        MyCharacterRes myCharacterRes = characterService.getMyCharacter(customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(myCharacterRes));
    }
}
