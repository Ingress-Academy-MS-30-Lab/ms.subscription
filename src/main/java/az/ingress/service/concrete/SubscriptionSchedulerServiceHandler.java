package az.ingress.service.concrete;

import az.ingress.client.PaymentClient;
import az.ingress.dao.entity.SubscriptionEntity;
import az.ingress.dao.repository.SubscriptionRepository;
import az.ingress.model.dto.PaymentRequestDto;
import az.ingress.model.enums.SubscriptionStatus;
import az.ingress.service.abstraction.SubscriptionSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static az.ingress.model.enums.PaymentStatus.SUCCESS;
import static az.ingress.model.enums.SubscriptionStatus.ACTIVE;
import static az.ingress.model.enums.SubscriptionStatus.EXPIRED;
import static az.ingress.model.enums.SubscriptionStatus.FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionSchedulerServiceHandler implements SubscriptionSchedulerService {
    private final SubscriptionRepository repository;
    private final PaymentClient paymentClient;

    private static final int INTERVAL_MINUTES = 30;
    private static final List<SubscriptionStatus> AUTO_RENEWABLE_STATUSES =
            List.of(ACTIVE, FAILED);

    @Override
    public void processAutoRenewals() {
        var now = LocalDateTime.now();
        var from = now.minusMinutes(INTERVAL_MINUTES);

        log.info("Auto-renew scheduler started. Interval: {} — {}", from, now);

        var subscriptions =
                repository.findAllAutoRenewableInRange(from, now, AUTO_RENEWABLE_STATUSES);

        subscriptions.forEach(this::renewSubscription);

        log.info("Auto-renew scheduler finished. Count: {}", subscriptions.size());
    }

    private void renewSubscription(SubscriptionEntity subscription) {
        if (subscription.getAutoRenew() == null || !subscription.getAutoRenew()) {
            subscription.setStatus(EXPIRED);
            log.info("Auto-renew disabled id={}", subscription.getId());
            repository.save(subscription);
            return;
        }

        var plan = subscription.getPlan();

        var paymentResponse = paymentClient.pay(
                PaymentRequestDto.builder()
                        .userId(subscription.getSupplierId())
                        .cardId(subscription.getCardId())
                        .amount(plan.getPrice())
                        .build()
        );

        if (paymentResponse != null && SUCCESS.equals(paymentResponse.getStatus())) {
            var now = LocalDateTime.now();
            var newEnd = plan.getPeriod().calculateEndDate(now);

            subscription.setStartDate(now);
            subscription.setEndDate(newEnd);
            subscription.setStatus(ACTIVE);
            subscription.setTransactionId(paymentResponse.getTransactionId());

            log.info("Subscription renewed id={}, newEnd={}", subscription.getId(), newEnd);
        } else {
            subscription.setStatus(FAILED);
            log.warn("Auto-renew failed id={}", subscription.getId());
        }

        repository.save(subscription);
    }
}
