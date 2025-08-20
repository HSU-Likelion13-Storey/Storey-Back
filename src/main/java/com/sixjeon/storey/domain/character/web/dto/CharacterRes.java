package com.sixjeon.storey.domain.character.web.dto;


public record CharacterRes(
        String imageUrl,
        String tagline,
        String name,
        String description,
        String narrativeSummary

) {
}
