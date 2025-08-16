package com.sixjeon.storey.global.external.toss.client;

/*
* 토스페이먼츠 API 통신 클래스
* */

import com.sixjeon.storey.domain.subscription.web.dto.PaymentConfirmReq;
import com.sixjeon.storey.global.config.TossPaymentsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

    // 최종 결제 승인 요청
    public Map<String,Object> requestPaymentConfirm(PaymentConfirmReq paymentConfirmReq) {
        String encodeKey = Base64.getEncoder().encodeToString((this.secretKey + ":").getBytes());

        try {
            return webClient.post()
                    .uri(this.apiUrl + "v1/payments/confirm")
                    .header("Authorization", "Basic " + encodeKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(Map.of(
                            "paymentKey", paymentConfirmReq.getPaymentKey(),
                            "orderId", paymentConfirmReq.getOrderId(),
                            "amount", paymentConfirmReq.getAmount()
                            ))
                    .retrieve()
                    .onStatus(status -> status.isError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> {
                                        log.error(body);
                                        return Mono.error(new PaymentFailedException());
                                    }))
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            log.error("Toss Payments 통신 중 오류 발생" , e);
            throw new PaymentFailedException();
        }
    }

    // 빌링키(자동 결제)를 요청
    public Map<String, Object> requestBillingPayment(String billingKey, Long amount,String orderId, String orderName){
        String encodeKey = Base64.getEncoder().encodeToString((this.secretKey + ":").getBytes());

        try {
                Map<String, Object> response = webClient.post()
                    .uri(this.apiUrl + "v1/billing" + billingKey )
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
                                        log.error(body);
                                        return Mono.error(new PaymentFailedException());
                                    }))
                    .bodyToMono(Map.class)
                    .block();

                if(response == null || !"DONE".equals(response.get("status"))){
                    throw new PaymentFailedException();
                }
                return response;
        } catch (Exception e) {
            log.error("Toss Payments 통신 중 오류 발생" , e);
            throw new PaymentFailedException();
        }
    }


}
