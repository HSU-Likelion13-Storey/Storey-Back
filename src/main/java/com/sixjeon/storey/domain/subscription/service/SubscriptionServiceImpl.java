package com.sixjeon.storey.domain.subscription.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.subscription.entity.Subscription;
import com.sixjeon.storey.domain.subscription.entity.enums.SubscriptionStatus;
import com.sixjeon.storey.domain.subscription.exception.SubscriptionNotFoundException;
import com.sixjeon.storey.domain.subscription.repository.SubscriptionRepository;
import com.sixjeon.storey.domain.subscription.web.dto.CardRegistrationReq;
import com.sixjeon.storey.domain.subscription.web.dto.PaymentConfirmReq;
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
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final OwnerRepository ownerRepository;
    private final TossPaymentsClient tossPaymentsClient;

    private final String PLAN_NAME = "마스코트 브랜딩 패스";
    private final Long PLAN_PRICE = 29000L;

    // [무료 체험 중]인 사장님이 결제 카드를 미리 등록하는 로직
    @Override
    @Transactional
    public void registerCard(CardRegistrationReq cardRegistrationReq, String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);
        Subscription subscription = subscriptionRepository.findByOwnerId(owner.getId())
                .orElseThrow(SubscriptionNotFoundException::new);
        subscription.registerCard(cardRegistrationReq.getCustomerKey());
    }

    @Override
    @Transactional
    public void reactivateSubscription(PaymentConfirmReq paymentConfirmReq, String ownerLoginId) {
        if (!Objects.equals(paymentConfirmReq.getAmount(), PLAN_PRICE)) {
            throw new PaymentFailedException();
        }
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        // 토스페이먼츠에 결제 승인 API 호출
        Map<String, Object> tossResponse = tossPaymentsClient.requestPaymentConfirm(paymentConfirmReq);

        String customerKey = (String) tossResponse.get("customerKey");

        if (customerKey == null) {
            throw new PaymentFailedException();
        }

        Subscription subscription = subscriptionRepository.findByOwnerId(owner.getId())
                .orElseThrow(SubscriptionNotFoundException::new);

        subscription.registerCard(customerKey);
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
                subscription.getCustomerKey() != null
        );
    }
    // 구독 취소
    @Override
    @Transactional
    public void cancelSubscription(String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);
        Subscription subscription = subscriptionRepository.findByOwnerId(owner.getId())
                .orElseThrow(SubscriptionNotFoundException::new);
        subscription.updateStatus(SubscriptionStatus.CANCELED);
    }

    @Override
    public void renewSubscription() {
        List<Subscription> targets = subscriptionRepository.findActiveSubscriptionEndingOn(
                LocalDate.now(),
                SubscriptionStatus.ACTIVE
        );
        for (Subscription subscription : targets) {
            Owner owner = subscription.getOwner();

            try {
                String orderId = "renew_" + subscription.getId() + "_" + LocalDate.now();

                tossPaymentsClient.requestBillingPayment(
                        subscription.getCustomerKey(),
                        PLAN_PRICE,
                        orderId,
                        PLAN_NAME
                );
                subscription.renew();
            } catch (Exception e) {
                subscription.updateStatus(SubscriptionStatus.EXPIRED);
            }
        }

    }


}
