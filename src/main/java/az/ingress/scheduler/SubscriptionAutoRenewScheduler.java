package az.ingress.scheduler;

import az.ingress.service.abstraction.SubscriptionSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionAutoRenewScheduler {

    private final SubscriptionSchedulerService subscriptionService;

    @Scheduled(cron = "0 */10 * * * *")
    @SchedulerLock(name = "autoRenewScheduler", lockAtLeastFor = "1m", lockAtMostFor = "5m")
    public void autoRenewSubscriptions() {
        log.info("Auto-renew scheduler triggered...");
        try {
            subscriptionService.processAutoRenewals();
        } catch (Exception e) {
            log.error("Error occurred during auto-renew scheduler execution", e);
        }
    }
}
