package com.sixjeon.storey.domain.character.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCharacterReq {
    String name;
    String tagline;
    String description;
    String narrativeSummary;
}
