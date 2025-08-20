package com.sixjeon.storey.domain.subscription.service;

import com.sixjeon.storey.domain.subscription.web.dto.CardRegistrationReq;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRenewReq;
import com.sixjeon.storey.domain.subscription.web.dto.SubscriptionRes;

public interface SubscriptionService {

    // [무료 체험 중]인 사장님이 결제 카드를 미리 등록하는 로직
    void registerCard(CardRegistrationReq cardRegistrationReq, String ownerLoginId);
    // 1개월 무료 구독 서비스 종료 후 즉시 결제해서 구독을 다시 활성화하는 로직
    void reactivateSubscription(SubscriptionRenewReq subscriptionRenewReq, String ownerLoginId);
    // 구독 상태 조회
    SubscriptionRes getSubscriptionStatus(String ownerLoginId);
    // 구독 취소
    void cancelSubscription(String ownerLoginId);

    // 자동 구독 갱신
    void renewSubscription();

    // 무료 체험 시작 로직
    void startFreeTrial(String ownerLoginId);

}
