package com.sixjeon.storey.domain.character.web.controller;

import com.sixjeon.storey.domain.character.service.CharacterService;
import com.sixjeon.storey.domain.character.web.dto.CharacterRes;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;
import com.sixjeon.storey.global.response.SuccessResponse;
import com.sixjeon.storey.global.security.details.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class CharacterController {

    private final CharacterService characterService;

    // 캐릭터 생성 & 인터뷰 한줄 요약
    @PostMapping("/character")
    public ResponseEntity<SuccessResponse<?>> character(@RequestBody @Valid InterviewReq interviewReq,
                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CharacterRes characterRes = characterService.generateCharacter(interviewReq, customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(characterRes));
    }
}
