package az.ingress.service.concrete;

import az.ingress.client.payment.PaymentClient;
import az.ingress.dto.request.PaymentRequestDto;
import az.ingress.dto.response.PaymentResponseDto;
import az.ingress.entity.Subscription;
import az.ingress.entity.SubscriptionPlan;
import az.ingress.enums.SubscriptionStatus;
import az.ingress.repository.SubscriptionRepository;
import az.ingress.service.abstraction.SubscriptionSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionSchedulerServiceHandler implements SubscriptionSchedulerService {

    private final SubscriptionRepository repository;
    private final PaymentClient paymentClient;

    @Value("${subscription.scheduler.interval-minutes}")
    private int intervalMinutes;

    @Override
    public void processAutoRenewals() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusMinutes(intervalMinutes);

        log.info("Auto-renew scheduler started. Interval: {} — {}", from, now);

        List<Subscription> subscriptions =
                repository.findAllAutoRenewableInRange(from, now);

        for (Subscription subscription : subscriptions) {
            renewSubscription(subscription);
        }

        log.info("Auto-renew scheduler finished. Count: {}", subscriptions.size());
    }

    private void renewSubscription(Subscription subscription) {
        SubscriptionPlan plan = subscription.getPlan();

        PaymentResponseDto paymentResponseDto = paymentClient.pay(PaymentRequestDto.builder()
                .userId(subscription.getSupplierId())
                .cardId(subscription.getCardId())
                .amount(plan.getPrice())
                .build());

        if (paymentResponseDto.getStatus().equals("success")) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime newEnd = plan.getPeriod().calculateEndDate(now);

            subscription.setStartDate(now);
            subscription.setEndDate(newEnd);
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setRenewCount(subscription.getRenewCount() + 1);
            subscription.setTransactionId(paymentResponseDto.getTransactionId());
            subscription.setRetryCount(0);

            log.info("Subscription renewed id={}, newEnd={}", subscription.getId(), newEnd);
        } else {
            subscription.setRetryCount(subscription.getRetryCount() + 1);
            log.warn("Auto-renew failed id={}, retryCount={}", subscription.getId(), subscription.getRetryCount());
        }

        if (subscription.getRetryCount() >= 3) {
            subscription.setStatus(SubscriptionStatus.FAILED);
        }

        repository.save(subscription);
    }
}

