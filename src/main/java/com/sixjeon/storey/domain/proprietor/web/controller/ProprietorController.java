package com.sixjeon.storey.domain.proprietor.web.controller;

import com.sixjeon.storey.domain.proprietor.service.ProprietorService;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorReq;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorRes;
import com.sixjeon.storey.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class ProprietorController {
    /**
     * 사업자번호 조회 API controller
     * BaseURL: {external.api.business-check.url}={serviceKey}
     * 1. GET | /api/proprietor 경로로 요청이 들어올 때
     * 2. 아래와 같은 형식으로 데이터가 들어온 것을 받아서 service 계층으로 넘겨준다.
     *  2-1. "1234567890"
     */
    private final ProprietorService proprietorService;

    @PostMapping("/proprietor")
    public ResponseEntity<SuccessResponse<?>> checkProprietor(@RequestBody @Valid CheckProprietorReq checkProprietorReq) {
        CheckProprietorRes checkProprietorRes = proprietorService.checkProprietorNumber(checkProprietorReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(checkProprietorRes));
    }


}
