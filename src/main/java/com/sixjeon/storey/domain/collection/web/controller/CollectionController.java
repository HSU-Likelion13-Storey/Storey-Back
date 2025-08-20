package com.sixjeon.storey.domain.collection.web.controller;

import com.sixjeon.storey.domain.collection.service.CollectionService;
import com.sixjeon.storey.domain.collection.web.dto.CollectionRes;
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
@RequestMapping("/user/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;
    // 모은 캐릭터 조회 메소드
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getUserCollection(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        CollectionRes collectionRes = collectionService.getUserCollection(customUserDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok(collectionRes));
    }
}
