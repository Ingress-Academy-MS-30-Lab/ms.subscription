package az.ingress.service.concrete;

import az.ingress.client.PaymentClient;
import az.ingress.dto.request.PaymentRequest;
import az.ingress.dto.request.RefundRequest;
import az.ingress.dto.request.SubscriptionRequest;
import az.ingress.dto.response.PaymentResponse;
import az.ingress.entity.Subscription;
import az.ingress.entity.SubscriptionPlan;
import az.ingress.enums.SubscriptionStatus;
import az.ingress.repository.SubscriptionPlanRepository;
import az.ingress.repository.SubscriptionRepository;
import az.ingress.service.abstraction.SubscriptionService;
import az.ingress.util.SubscriptionPeriodUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceHandler implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentClient paymentClient;

    @Override
    public void createSubscription(SubscriptionRequest request) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(request.getSubscriptionPlanId())
                .orElseThrow(() -> new RuntimeException("Subscription Plan Not Found"));

        if (subscriptionRepository.existsBySupplierIdAndProductId(
                request.getSupplierId(),
                subscriptionPlan.getProductId())) {
            throw new RuntimeException("Subscription already exists");
        }

        PaymentResponse paymentResponse = paymentClient.pay(PaymentRequest.builder()
                .cardId(request.getCardId())
                .userId(request.getSupplierId())
                .amount(subscriptionPlan.getPrice())
                .build()
        );

        if (!paymentResponse.getStatus().equals("SUCCESS")) {
            throw new RuntimeException();
        }

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = SubscriptionPeriodUtil.calculateEndDate(startDate, subscriptionPlan.getPeriod());

        Subscription subscription = Subscription.builder()
                .supplierId(request.getSupplierId())
                .status(SubscriptionStatus.ACTIVE)
                .autoRenew(request.isAutoRenew())
                .plan(subscriptionPlan)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    public void renewSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription Not Found"));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(Long id, boolean immediateRefund) {
        Subscription subscription = subscriptionRepository
                .findByIdAndStatus(id, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Subscription Not Found"));

        LocalDateTime now = LocalDateTime.now();

        if (immediateRefund) {
            boolean refundable = subscription.getStartDate()
                    .plusDays(1)
                    .isAfter(now);

            if (!refundable) {
                throw new RuntimeException("Payment cannot be cancelled after 1 day");
            }

            RefundRequest refundRequest = RefundRequest.builder()
                    .transactionId(subscription.getTransactionId())
                    .amount(subscription.getPlan().getPrice())
                    .reason("User chose immediate cancellation")
                    .build();

            PaymentResponse refundResponse = paymentClient.refund(refundRequest);
            if (!"success".equalsIgnoreCase(refundResponse.getStatus())) {
                throw new RuntimeException("Refund failed");
            }

            subscription.setStatus(SubscriptionStatus.CANCELLED);
            subscriptionRepository.save(subscription);
        } else {
            subscription.setAutoRenew(false);
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void enableAutoRenew(Long id) {
        Subscription subscription = subscriptionRepository
                .findByIdAndStatus(id, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Subscription Not Found"));

        subscription.setAutoRenew(true);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void disableAutoRenew(Long id) {
        Subscription subscription = subscriptionRepository
                .findByIdAndStatus(id, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Subscription Not Found"));

        subscription.setAutoRenew(false);
        subscriptionRepository.save(subscription);
    }
}
