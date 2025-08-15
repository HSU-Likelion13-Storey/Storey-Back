package com.sixjeon.storey.domain.subscription.service;

import com.sixjeon.storey.domain.subscription.web.dto.CardRegistrationReq;

public interface SubscriptionService {

    // [무료 체험 중]인 사장님이 결제 카드를 미리 등록하는 로직
    void registerCard(CardRegistrationReq cardRegistrationReq, String ownerLoginId);

}
