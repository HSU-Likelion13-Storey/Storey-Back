package com.sixjeon.storey.domain.character.web.dto;

public record CharacterDetailRes(
        Long characterId,
        String imageUrl,
        String tagline,
        String name,
        String description,
        String narrativeSummary,
        String storeName,
        String addressMain,
        String addressDetail
) {

}
