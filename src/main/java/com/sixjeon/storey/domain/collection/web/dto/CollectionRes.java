package com.sixjeon.storey.domain.collection.web.dto;

import java.util.List;

public record CollectionRes(
        List<CollectedCharacterRes> collectedCharacters,
        int totalCount
) {
}
