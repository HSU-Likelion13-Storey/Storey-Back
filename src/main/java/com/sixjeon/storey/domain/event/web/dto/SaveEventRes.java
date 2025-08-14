package com.sixjeon.storey.domain.event.web.dto;

import lombok.Builder;

@Builder
public record SaveEventRes(
        Long eventId,
        String content
) {
}
