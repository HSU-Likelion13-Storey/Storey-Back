package com.sixjeon.storey.global.external.toss.client;

/*
* 토스페이먼츠 API 통신 클래스
* */

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.sixjeon.storey.global.external.toss.exception.PaymentFailedException;

import java.util.Base64;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class TossPaymentsClient {

    private final WebClient webClient;
    private final String secretKey;
    private final String apiUrl;

    public Map<String, Object> confirmPayment(String paymentKey, String orderId, Long amount, String customerKey) {
        String encodeKey = Base64.getEncoder().encodeToString((this.secretKey + ":").getBytes());

        try {
            Map<String, Object> requestBody = Map.of(
                    "paymentKey", paymentKey,
                    "orderId", orderId,
                    "amount", amount,
                    "customerKey", customerKey  // 빌링키 자동발급을 위한 customerKey 추가
            );

            Map<String, Object> response = webClient.post()
                    .uri("v1/payments/confirm")
                    .header("Authorization", "Basic " + encodeKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(status -> status.isError(),
                            response2 -> response2.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new PaymentFailedException())))
                    .bodyToMono(Map.class)
                    .block();

            // 응답에 billingKey가 포함됨 (자동발급)
            return response;
        } catch (Exception e) {
            throw new PaymentFailedException();
        }
    }

    public Map<String, Object> requestBillingPayment(String billingKey, Long amount, String orderId, String orderName) {
        String encodeKey = Base64.getEncoder().encodeToString((this.secretKey + ":").getBytes());

        try {
            Map<String, Object> response = webClient.post()
                    .uri("v1/billing/" + billingKey)
                    .header("Authorization", "Basic " + encodeKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(Map.of(
                            "amount", amount,
                            "orderId", orderId,
                            "orderName", orderName
                    ))
                    .retrieve()
                    .onStatus(status -> status.isError(),
                            response2 -> response2.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new PaymentFailedException())))
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !"DONE".equals(response.get("status"))) {
                throw new PaymentFailedException();
            }
            return response;
        } catch (Exception e) {
            throw new PaymentFailedException();
        }
    }

}
