package com.sixjeon.storey.domain.subscription.web.controller;

import com.sixjeon.storey.domain.subscription.service.SubscriptionService;
import com.sixjeon.storey.domain.subscription.web.dto.CardRegistrationReq;
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
@RequestMapping("/owner/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // 무료 체험 중 결제 카드 등록 API
    @PostMapping("/card")
    public ResponseEntity<SuccessResponse<?>> registerCard(@RequestBody @Valid CardRegistrationReq cardRegistrationReq,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        subscriptionService.registerCard(cardRegistrationReq, customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok("카드 정보 성공적으로 등록되었습니다."));
    }
}
