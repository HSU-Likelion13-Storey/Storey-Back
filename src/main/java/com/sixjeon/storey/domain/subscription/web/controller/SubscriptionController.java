package com.sixjeon.storey.domain.subscription.web.controller;

import com.sixjeon.storey.domain.subscription.service.SubscriptionService;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRenewReq;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRenewRes;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRes;
import com.sixjeon.storey.global.response.SuccessResponse;
import com.sixjeon.storey.global.security.details.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    // 구독 재활성화
    @PostMapping("/renew")
    public ResponseEntity<SuccessResponse<?>> renewSubscription(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                 @RequestBody @Valid SubscriptionRenewReq subscriptionRenewReq) {
        boolean isSuccess = subscriptionService.subscribeWithImmediatePayment(subscriptionRenewReq, customUserDetails.getUsername());

        if (isSuccess) {
            SubscriptionRenewRes subscriptionRenewRes = new SubscriptionRenewRes(true);
            return ResponseEntity.ok(SuccessResponse.ok(subscriptionRenewRes));
        } else {
            SubscriptionRenewRes subscriptionRenewRes = new SubscriptionRenewRes(false);
            return ResponseEntity.ok(SuccessResponse.ok(subscriptionRenewRes));
        }

    }

    // 구독 상태 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getSubscriptionStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        SubscriptionRes subscriptionRes = subscriptionService.getSubscriptionStatus(customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok(subscriptionRes));
    }

    // 구독 취소
    // 취소는 삭제가 아니기 때문 deleteMapping 아님
    @PutMapping("/cancel")
    public ResponseEntity<SuccessResponse<?>> cancelSubscription(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        subscriptionService.cancelSubscription(customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok("구독 취소가 성공적으로 완료되었습니다."));
    }

    // 무료 체험 시작 API
    @PostMapping("/trial")
    public ResponseEntity<SuccessResponse<?>> startTrial(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        subscriptionService.startFreeTrial(customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok("무료 체험 시작이 성공적으로 완료되었습니다."));
    }

}
