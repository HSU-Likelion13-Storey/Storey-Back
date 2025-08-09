package com.sixjeon.storey.domain.store.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterStoreReq {
    @NotBlank
    private String storeName;
    @NotBlank
    private String representativeName;
    @NotBlank
    private String businessNumber;

    private String businessType;
    private String businessCategory;

    @NotBlank
    private String addressMain;

    private String addressDetail;

    @NotBlank
    private String postalCode;
}
