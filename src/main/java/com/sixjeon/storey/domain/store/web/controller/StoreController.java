package com.sixjeon.storey.domain.store.web.controller;

import com.sixjeon.storey.domain.store.service.StoreService;
import com.sixjeon.storey.domain.store.web.dto.RegisterStoreReq;
import com.sixjeon.storey.global.response.SuccessResponse;
import com.sixjeon.storey.global.security.details.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> registerStore(
            @Valid @RequestBody RegisterStoreReq registerStoreReq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        storeService.registerStore(registerStoreReq, customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.created("가게 정보 성공정으로 등록되었습니다."));
            }
}
