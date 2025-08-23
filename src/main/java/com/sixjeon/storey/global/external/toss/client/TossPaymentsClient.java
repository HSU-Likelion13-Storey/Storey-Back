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

    // 빌링키 발급 API (authKey -> customerKey 변환)
    public String issueBillingKey(String authKey, String customerKey) {
        String encodeKey = Base64.getEncoder().encodeToString((this.secretKey + ":").getBytes());

        try {
            Map<String, Object> response = webClient.post()
                    .uri("v1/billing/authorizations/issue")
                    .header("Authorization", "Basic " + encodeKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(Map.of(
                            "authKey", authKey,
                            "customerKey", customerKey
                    ))
                    .retrieve()
                    .onStatus(status -> status.isError(),
                            response2 -> response2.bodyToMono(String.class)
                                    .flatMap(body -> {
                                        return Mono.error(new PaymentFailedException());
                                    }))
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || response.get("billingKey") == null) {
                throw new PaymentFailedException();
            }

            return (String) response.get("billingKey");
        } catch (Exception e) {
            throw new PaymentFailedException();
        }
    }

    // 빌링키(자동 결제)를 요청
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
                                    .flatMap(body -> {
                                        return Mono.error(new PaymentFailedException());
                                    }))
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

    public Map<String, Object > confirmPayment(String paymentKey, String orderId, Long amount){
        String encodeKey = Base64.getEncoder().encodeToString((this.secretKey + ":").getBytes());

        try {
            Map<String, Object> response = webClient.post()
                    .uri("v1/payments/confirm")
                    .header("Authorization", "Basic " + encodeKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(Map.of(
                            "paymentKey", paymentKey,
                            "orderId", orderId,
                            "amount", amount
                    ))
                    .retrieve()
                    .onStatus(status -> status.isError(),
                            response2 -> response2.bodyToMono(String.class)
                                    .flatMap(body ->
                                        Mono.error(new PaymentFailedException())
                                    ))
                    .bodyToMono(Map.class)
                    .block();

            return response;
        } catch (Exception e) {
            throw new PaymentFailedException();
        }
    }

}
