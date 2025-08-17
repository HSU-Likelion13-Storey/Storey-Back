package com.sixjeon.storey.domain.subscription.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.subscription.entity.Subscription;
import com.sixjeon.storey.domain.subscription.entity.enums.SubscriptionStatus;
import com.sixjeon.storey.domain.subscription.exception.InvalidSubscriptionStatusException;
import com.sixjeon.storey.domain.subscription.exception.SubscriptionNotFoundException;
import com.sixjeon.storey.domain.subscription.repository.SubscriptionRepository;
import com.sixjeon.storey.domain.subscription.web.dto.CardRegistrationReq;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRenewReq;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRes;
import com.sixjeon.storey.global.external.toss.client.TossPaymentsClient;
import com.sixjeon.storey.global.external.toss.exception.PaymentFailedException;
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
    private final Long PLAN_PRICE = 20900L;

    // 카드 등록 로직
    @Override
    @Transactional
    public void registerCard(CardRegistrationReq cardRegistrationReq, String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        // Owner ID를 customerKey로 사용
        String customerKey = "owner_" + owner.getId();

        // 토스 페이먼트 API를 통해 authKey로 빌링키 발금
        String billingKey = tossPaymentsClient.issueBillingKey(
                cardRegistrationReq.getAuthKey(),
                customerKey
        );

        owner.registerCard(billingKey);

        ownerRepository.save(owner);

    }

    @Override
    @Transactional
    public void reactivateSubscription(SubscriptionRenewReq subscriptionRenewReq, String ownerLoginId) {

        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        // 구독 정보 조회 및 갱신
        Subscription subscription = subscriptionRepository.findByOwnerId(owner.getId())
                .orElseThrow(SubscriptionNotFoundException::new);

        if (!owner.hasRegisteredCard()) {
            throw new PaymentFailedException();
        }

        // orderId가 비어있으면 자동 생성
        String orderId = subscriptionRenewReq.getOrderId();
        if (orderId == null || orderId.trim().isEmpty()) {
            orderId = "subscription_renew_" + owner.getId() + "_" + System.currentTimeMillis();
        }

        // 토스페이먼츠에 결제 승인 API 호출
        Map<String, Object> tossResponse = tossPaymentsClient.requestBillingPayment(
                owner.getBillingKey(),
                PLAN_PRICE,
                orderId,
                PLAN_NAME);

        String status = (String) tossResponse.get("status");

        if (!"DONE".equals(status)) {
            throw new PaymentFailedException();
        }

        subscription.renew();
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
