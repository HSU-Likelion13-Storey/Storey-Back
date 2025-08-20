package com.sixjeon.storey.domain.store.web.controller;

import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.repository.StoreRepository;
import com.sixjeon.storey.domain.store.service.StoreService;
import com.sixjeon.storey.domain.store.web.dto.MapStoreRes;
import com.sixjeon.storey.domain.store.web.dto.RegisterStoreReq;
import com.sixjeon.storey.domain.store.web.dto.StoreDetailRes;
import com.sixjeon.storey.global.response.SuccessResponse;
import com.sixjeon.storey.global.security.details.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;

    @PostMapping("/owner/store")
    public ResponseEntity<SuccessResponse<?>> registerStore(
            @Valid @RequestBody RegisterStoreReq registerStoreReq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        storeService.registerStore(registerStoreReq, customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.created("가게 정보 성공정으로 등록되었습니다."));
    }

    // 지도에서 모든 가게 조회
    @GetMapping("/stores/map")
    public ResponseEntity<SuccessResponse<?>> getStoresForMap(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MapStoreRes> mapStoreRes = storeService.findAllStoresForUserMap(customUserDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok(mapStoreRes));
    }




}
