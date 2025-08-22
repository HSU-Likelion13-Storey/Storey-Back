package com.sixjeon.storey.global.external.kakao.service;

import com.sixjeon.storey.domain.store.exception.AddressConversionFailedException;
import com.sixjeon.storey.global.external.kakao.dto.DocumentDto;
import com.sixjeon.storey.global.external.kakao.dto.KakaoAddressRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final WebClient webClient;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${kakao.api.url.address}")
    private String kakaoApiUrl;

    public Mono<DocumentDto> getCoordinates(String address) {

        String url = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", address)
                .build().
                toUriString();

        return webClient.get()
                .uri(url)
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .retrieve()
                .bodyToMono(KakaoAddressRes.class)
                .map(response -> {
                    if (response.getDocuments() == null || response.getDocuments().isEmpty()) {
                        throw new AddressConversionFailedException();
                    }
                    return response.getDocuments().get(0);
                })
                .doOnError(error -> log.error("카카오 주소 변환 API 호출 중 오류 발생: {}", error.getMessage()));

    }
}
