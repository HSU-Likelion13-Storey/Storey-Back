package com.sixjeon.storey.domain.event.web.controller;

import com.sixjeon.storey.domain.event.service.EventService;
import com.sixjeon.storey.domain.event.web.dto.SaveEventReq;
import com.sixjeon.storey.domain.event.web.dto.SaveEventRes;
import com.sixjeon.storey.global.response.SuccessResponse;
import com.sixjeon.storey.global.security.details.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner/store/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // 가게 이벤트 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> findStoreEvent(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        SaveEventRes saveEventRes = eventService.findStoreEvent(customUserDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok(saveEventRes));

    }

    // 가게 이벤트 생성 및 수정
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createOrUpdateEvent(@Valid @RequestBody SaveEventReq saveEventReq,
                                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        eventService.createOrUpdateEvent(saveEventReq, customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok("이벤트 정보 성공적으로 등록되었습니다."));
    }

    // 가게 이벤트 삭제
    @DeleteMapping
    public ResponseEntity<SuccessResponse<?>> deleteEvent(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        eventService.deleteEvent(customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.ok("이벤트 정보 성공적으로 삭제되었습니다."));
    }

}
