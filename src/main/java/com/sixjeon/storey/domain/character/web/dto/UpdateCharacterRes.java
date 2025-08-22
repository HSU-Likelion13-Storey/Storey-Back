package com.sixjeon.storey.domain.character.web.dto;

import lombok.Builder;

@Builder
public record UpdateCharacterRes(
        Long characterId,
        String name,
        String description,
        String narrativeSummary,
        String tagline
) {
}
