package com.sixjeon.storey.domain.owner.web.controller;

import com.sixjeon.storey.domain.owner.service.OwnerService;
import com.sixjeon.storey.domain.owner.web.dto.OwnerMyPageRes;
import com.sixjeon.storey.global.response.SuccessResponse;
import com.sixjeon.storey.global.security.details.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping("/mypage")
    public ResponseEntity<SuccessResponse<?>> getMyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        OwnerMyPageRes myPageInfo  = ownerService.getMyPage(customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(myPageInfo));
    }
}
