package com.sixjeon.storey.domain.user.web.controller;

import com.sixjeon.storey.domain.user.service.UserService;
import com.sixjeon.storey.domain.user.web.dto.UserMyPageRes;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/mypage")
    public ResponseEntity<SuccessResponse<?>> getMyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        UserMyPageRes myPageInfo  = userService.getMyPage(customUserDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(myPageInfo));
    }
}
