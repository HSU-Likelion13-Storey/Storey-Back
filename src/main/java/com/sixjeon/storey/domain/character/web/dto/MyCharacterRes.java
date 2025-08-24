package com.sixjeon.storey.domain.character.web.dto;

import lombok.Builder;

@Builder
public record MyCharacterRes(
        boolean hasCharacter,
        Long characterId,
        String imageUrl,
        String tagline,
        String name,
        String description,
        String narrativeSummary
) {
    // 캐릭터가 없는 경우를 위한 정적 팩토리 메소드
    public static MyCharacterRes noCharacter() {
        return MyCharacterRes.builder()
                .hasCharacter(false)
                .characterId(null)
                .imageUrl(null)
                .tagline(null)
                .name(null)
                .description(null)
                .narrativeSummary(null)
                .build();
    }
}
