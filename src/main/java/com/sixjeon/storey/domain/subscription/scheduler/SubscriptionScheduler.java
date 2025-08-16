package com.sixjeon.storey.domain.subscription.scheduler;

import com.sixjeon.storey.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 7 * * *")
    public void renewSubscription() {
        log.info("구독 자동 갱신 스케줄러 시작");
        subscriptionService.renewSubscription();
        log.info("구독 자동 갱신 스케줄러 완료");
    }
}
