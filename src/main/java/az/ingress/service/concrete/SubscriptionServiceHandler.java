package az.ingress.service.concrete;

import az.ingress.client.payment.PaymentClient;
import az.ingress.dto.request.PaymentRequestDto;
import az.ingress.dto.request.RefundRequestDto;
import az.ingress.dto.request.SubscriptionRequest;
import az.ingress.dto.response.PaymentResponseDto;
import az.ingress.entity.Subscription;
import az.ingress.entity.SubscriptionPlan;
import az.ingress.enums.SubscriptionStatus;
import az.ingress.exception.BadRequestException;
import az.ingress.exception.ResourceNotFoundException;
import az.ingress.repository.SubscriptionPlanRepository;
import az.ingress.repository.SubscriptionRepository;
import az.ingress.service.abstraction.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static az.ingress.exception.ErrorMessageKey.PAYMENT_FAILED;
import static az.ingress.exception.ErrorMessageKey.PAYMENT_REFUND_FAILED;
import static az.ingress.exception.ErrorMessageKey.SUBSCRIPTION_IS_NOT_REFUNDABLE;
import static az.ingress.exception.ErrorMessageKey.SUBSCRIPTION_NOT_FOUND;
import static az.ingress.exception.ErrorMessageKey.SUBSCRIPTION_PLAN_EXISTS;
import static az.ingress.exception.ErrorMessageKey.SUBSCRIPTION_PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceHandler implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentClient paymentClient;

    @Override
    public void createSubscription(SubscriptionRequest request) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(request.getSubscriptionPlanId())
                .orElseThrow(() -> new ResourceNotFoundException(SUBSCRIPTION_PLAN_NOT_FOUND, request.getSubscriptionPlanId()));

        if (subscriptionRepository.existsBySupplierIdAndProductId(
                request.getSupplierId(),
                subscriptionPlan.getProductId())) {
            throw new BadRequestException(SUBSCRIPTION_PLAN_EXISTS, request.getSupplierId(), subscriptionPlan.getProductId());
        }

        PaymentResponseDto paymentResponseDto = paymentClient.pay(PaymentRequestDto.builder()
                .cardId(request.getCardId())
                .userId(request.getSupplierId())
                .amount(subscriptionPlan.getPrice())
                .build()
        );

        if (!"SUCCESS".equals(paymentResponseDto.getStatus())) {
            throw new BadRequestException(PAYMENT_FAILED);
        }

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = subscriptionPlan.getPeriod().calculateEndDate(startDate);

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
        Subscription subscription = getSubscription(id);

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(Long id, boolean immediateRefund) {
        Subscription subscription = getSubscription(id);

        LocalDateTime now = LocalDateTime.now();

        if (immediateRefund) {
            boolean refundable = subscription.getStartDate()
                    .plusDays(1)
                    .isAfter(now);

            if (!refundable) {
                throw new BadRequestException(SUBSCRIPTION_IS_NOT_REFUNDABLE);
            }

            RefundRequestDto refundRequest = RefundRequestDto.builder()
                    .transactionId(subscription.getTransactionId())
                    .amount(subscription.getPlan().getPrice())
                    .reason("User chose immediate cancellation")
                    .build();

            PaymentResponseDto refundResponse = paymentClient.refund(refundRequest);
            if (!"success".equalsIgnoreCase(refundResponse.getStatus())) {
                throw new BadRequestException(PAYMENT_REFUND_FAILED);
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
        Subscription subscription = getSubscription(id);

        subscription.setAutoRenew(true);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void disableAutoRenew(Long id) {
        Subscription subscription = getSubscription(id);

        subscription.setAutoRenew(false);
        subscriptionRepository.save(subscription);
    }

    private Subscription getSubscription(Long id) {
        return subscriptionRepository
                .findByIdAndStatus(id, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(SUBSCRIPTION_NOT_FOUND, id));
    }
}
