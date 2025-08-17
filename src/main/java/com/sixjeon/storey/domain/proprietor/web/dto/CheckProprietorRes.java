package com.sixjeon.storey.domain.proprietor.web.dto;

import java.time.LocalDateTime;

public record CheckProprietorRes(
        String tax_type,
        boolean pass
) {
}
