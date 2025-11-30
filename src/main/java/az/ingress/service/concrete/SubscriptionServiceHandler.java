package az.ingress.service.concrete;

import az.ingress.client.PaymentClient;
import az.ingress.dao.entity.SubscriptionEntity;
import az.ingress.dao.repository.SubscriptionPlanRepository;
import az.ingress.dao.repository.SubscriptionRepository;
import az.ingress.exception.BadRequestException;
import az.ingress.exception.NotFoundException;
import az.ingress.model.dto.PaymentRequestDto;
import az.ingress.model.dto.RefundRequestDto;
import az.ingress.model.request.SubscriptionRequest;
import az.ingress.service.abstraction.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static az.ingress.exception.ErrorMessage.PAYMENT_FAILED;
import static az.ingress.exception.ErrorMessage.PAYMENT_REFUND_FAILED;
import static az.ingress.exception.ErrorMessage.SUBSCRIPTION_IS_NOT_REFUNDABLE;
import static az.ingress.exception.ErrorMessage.SUBSCRIPTION_NOT_FOUND;
import static az.ingress.exception.ErrorMessage.SUBSCRIPTION_PLAN_EXISTS;
import static az.ingress.exception.ErrorMessage.SUBSCRIPTION_PLAN_NOT_FOUND;
import static az.ingress.model.enums.PaymentStatus.SUCCESS;
import static az.ingress.model.enums.SubscriptionStatus.ACTIVE;
import static az.ingress.model.enums.SubscriptionStatus.CANCELLED;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceHandler implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentClient paymentClient;

    private static final String CANCEL_REASON = "User chose immediate cancellation";

    @Override
    public void createSubscription(String supplierId, SubscriptionRequest request) {
        var subscriptionPlan = subscriptionPlanRepository.findById(request.getSubscriptionPlanId())
                .orElseThrow(() -> new NotFoundException(SUBSCRIPTION_PLAN_NOT_FOUND, request.getSubscriptionPlanId()));

        if (subscriptionRepository.existsBySupplierIdAndProductId(
                supplierId,
                subscriptionPlan.getProductId())) {
            throw new BadRequestException(SUBSCRIPTION_PLAN_EXISTS, supplierId, subscriptionPlan.getProductId());
        }

        var paymentResponseDto = paymentClient.pay(PaymentRequestDto.builder()
                .cardId(request.getCardId())
                .userId(supplierId)
                .amount(subscriptionPlan.getPrice())
                .build()
        );

        if (!SUCCESS.equals(paymentResponseDto.getStatus())) {
            throw new BadRequestException(PAYMENT_FAILED);
        }

        var startDate = LocalDateTime.now();
        var endDate = subscriptionPlan.getPeriod().calculateEndDate(startDate);

        var subscription = SubscriptionEntity.builder()
                .supplierId(supplierId)
                .status(ACTIVE)
                .autoRenew(request.isAutoRenew())
                .plan(subscriptionPlan)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(String supplierId, Long id) {
        var subscription = getSubscription(supplierId, id);

        var now = LocalDateTime.now();

        boolean refundable = subscription.getStartDate()
                .plusDays(1)
                .isAfter(now);

        if (!refundable) {
            throw new BadRequestException(SUBSCRIPTION_IS_NOT_REFUNDABLE);
        }

        var refundRequest = RefundRequestDto.builder()
                .transactionId(subscription.getTransactionId())
                .amount(subscription.getPlan().getPrice())
                .reason(CANCEL_REASON)
                .build();

        var refundResponse = paymentClient.refund(refundRequest);
        if (!SUCCESS.equals(refundResponse.getStatus())) {
            throw new BadRequestException(PAYMENT_REFUND_FAILED);
        }

        subscription.setStatus(CANCELLED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void updateAutoRenew(String supplierId, Long id, boolean enabled) {
        var subscription = getSubscription(supplierId, id);
        subscription.setAutoRenew(enabled);
        subscriptionRepository.save(subscription);
    }

    private SubscriptionEntity getSubscription(String supplierId, Long id) {
        return subscriptionRepository
                .findByIdAndSupplierIdAndStatus(id, supplierId, ACTIVE)
                .orElseThrow(() -> new NotFoundException(SUBSCRIPTION_NOT_FOUND, id));
    }
}
