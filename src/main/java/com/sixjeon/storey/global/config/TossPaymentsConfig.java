package com.sixjeon.storey.global.config;

import com.sixjeon.storey.global.external.toss.client.TossPaymentsClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Configuration
// 토스페이먼츠 관련 설정 관리
public class TossPaymentsConfig {

    @Value("${payments.toss.secret-key}")
    private String secretKey;

    @Value("${payments.toss.api-url}")
    private String apiUrl;

    @Bean
    public TossPaymentsClient tossPaymentsClient(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder
                .baseUrl(apiUrl)
                .build();
        return new TossPaymentsClient(webClient, secretKey, apiUrl);
    }

}
