package com.sixjeon.storey.domain.proprietor.service;

import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorReq;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProprietorService {
    /**
     * 사업자번호 조회 API Service
     * BaseURL: https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey={serviceKey}
     * 1. Controller 에게 넘겨받은 사업자번호 문자열 값을 1개의 값만 담을 수 있는 배열로 만든다.
     * 2. BaseURL + serviceKey를 가지고 요청을 한다.
     * 3. 반환값 중 tax_type 만 뽑아서 Res를 만든다.
     */
    @Value("${external.api.business-check.url}")
    private String baseUrl;

    @Value("${external.api.business-check.key}")
    private String serviceKey;

    private final WebClient.Builder webClientBuilder;

    public CheckProprietorRes checkProprietorNumber(CheckProprietorReq checkProprietorReq) {
        String businessNumber = checkProprietorReq.getBusinessNumber();

        Map<String, Object> requestBody = Collections.singletonMap("b_no", List.of(businessNumber));

        String fullUrl = baseUrl + "?serviceKey=" + serviceKey;

        Map response = webClientBuilder.build().post()
                .uri(URI.create(fullUrl))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
        String taxType = (String) dataList.get(0).get("tax_type");
        boolean pass = true;
        if (taxType.equals("국세청에 등록되지 않은 사업자등록번호입니다."))
            pass = false;

        return new CheckProprietorRes(taxType, pass);
    }

}
