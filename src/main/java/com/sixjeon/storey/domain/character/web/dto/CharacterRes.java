package com.sixjeon.storey.domain.character.web.dto;


public record CharacterRes(
        Long characterId,
        Long interviewSessionId,
        String imageUrl,
        String tagline,
        String name,
        String description,
        String narrativeSummary

) {
}
