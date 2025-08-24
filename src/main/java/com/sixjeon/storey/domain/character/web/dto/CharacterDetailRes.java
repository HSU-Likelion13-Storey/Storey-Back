package com.sixjeon.storey.domain.character.web.dto;

public record CharacterDetailRes(
        Long characterId,
        Long storeId,
        Long interviewSessionId,
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
