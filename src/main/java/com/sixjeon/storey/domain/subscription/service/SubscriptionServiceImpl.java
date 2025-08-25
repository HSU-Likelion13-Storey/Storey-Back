package com.sixjeon.storey.domain.subscription.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.subscription.entity.Subscription;
import com.sixjeon.storey.domain.subscription.entity.enums.SubscriptionStatus;
import com.sixjeon.storey.domain.subscription.exception.InvalidSubscriptionStatusException;
import com.sixjeon.storey.domain.subscription.exception.SubscriptionNotFoundException;
import com.sixjeon.storey.domain.subscription.repository.SubscriptionRepository;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRenewReq;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRes;
import com.sixjeon.storey.global.external.toss.client.TossPaymentsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final OwnerRepository ownerRepository;
    private final TossPaymentsClient tossPaymentsClient;

    private final String PLAN_NAME = "마스코트 브랜딩 패스";
    private final Long PLAN_PRICE = 29900L;

    @Override
    @Transactional
    public boolean subscribeWithImmediatePayment(SubscriptionRenewReq subscriptionRenewReq, String ownerLoginId) {
        try {
            Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                    .orElseThrow(UserNotFoundException::new);

            Subscription subscription = subscriptionRepository.findByOwnerId(owner.getId())
                    .orElseThrow(SubscriptionNotFoundException::new);

            String customerKey = "owner_" + owner.getId();

            // 즉시 결제 실행
            Map<String, Object> tossResponse = tossPaymentsClient.confirmPayment(
                    subscriptionRenewReq.getPaymentKey(),
                    subscriptionRenewReq.getOrderId(),
                    subscriptionRenewReq.getAmount(),
                    customerKey
            );

            String status = (String) tossResponse.get("status");

            if ("DONE".equals(status)) {
                // 결제 성공 후 빌링키 자동 발급 (토스페이먼츠 API 응답에서 추출)
                String billingKey = (String) tossResponse.get("billingKey");

                if (billingKey != null) {
                    // 자동 발급된 빌링키를 Owner에게 등록
                    owner.registerCard(billingKey);
                    ownerRepository.save(owner);
                }

                // 구독 활성화
                subscription.renew();
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("즉시결제 및 카드등록 실패: {}", e.getMessage());
            return false;
        }
    }

    // 구독 상태 조회
    @Override
    @Transactional
    public SubscriptionRes getSubscriptionStatus(String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);
        Subscription subscription = subscriptionRepository.findByOwnerId(owner.getId())
                .orElseThrow(SubscriptionNotFoundException::new);
        return new SubscriptionRes(
                subscription.getPlanName(),
                subscription.getStatus().name(),
                subscription.getEndDate(),
                owner.hasRegisteredCard()        );
    }
    // 구독 취소
    @Override
    @Transactional
    public void cancelSubscription(String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);
        Subscription subscription = subscriptionRepository.findByOwnerId(owner.getId())
                .orElseThrow(SubscriptionNotFoundException::new);
        subscription.updateStatus(SubscriptionStatus.CANCELED_REQUESTED);
    }

    @Override
    public void renewSubscription() {
        // ACTIVE 상태인 구독 중 만료되는 것들 처리
        List<Subscription> targets = subscriptionRepository.findActiveSubscriptionEndingOn(
                LocalDate.now(),
                SubscriptionStatus.ACTIVE
        );
        for (Subscription subscription : targets) {
            Owner owner = subscription.getOwner();

            try {
                String orderId = "renew_" + subscription.getId() + "_" + LocalDate.now();

                tossPaymentsClient.requestBillingPayment(
                        owner.getBillingKey(),
                        PLAN_PRICE,
                        orderId,
                        PLAN_NAME
                );
                subscription.renew();
            } catch (Exception e) {
                subscription.updateStatus(SubscriptionStatus.EXPIRED);
            }
        }
        // CANCELED_REQUESTED 상태인 구독 중 만료되는 것들 처리
        List<Subscription> canceledRequests = subscriptionRepository.findActiveSubscriptionEndingOn(
                LocalDate.now(),
                SubscriptionStatus.CANCELED_REQUESTED
        );
        for (Subscription subscription : canceledRequests) {
            subscription.updateStatus(SubscriptionStatus.CANCELED);
        }

    }

    @Override
    @Transactional
    public void startFreeTrial(String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        Subscription subscription = subscriptionRepository.findByOwnerId(owner.getId())
                .orElseThrow(SubscriptionNotFoundException::new);

        if (!SubscriptionStatus.TRIAL_AVAILABLE.equals(subscription.getStatus())) {
                throw new InvalidSubscriptionStatusException();
        }
        subscription.startTrial();
    }
}
