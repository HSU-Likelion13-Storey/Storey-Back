package com.sixjeon.storey.domain.event.web.controller;

import com.sixjeon.storey.domain.event.service.EventService;
import com.sixjeon.storey.domain.event.web.dto.SaveEventRes;
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

}
