package az.ingress.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    PRODUCT_NOT_FOUND("product.not.found"),
    SUBSCRIPTION_PLAN_NOT_FOUND("subscription.plan.not.found"),
    SUBSCRIPTION_PLAN_EXISTS("subscription.plan.exists"),
    SUBSCRIPTION_NOT_FOUND("subscription.not.found"),
    PAYMENT_FAILED("payment.failed"),
    SUBSCRIPTION_IS_NOT_REFUNDABLE("subscription.is.not.refundable"),
    PAYMENT_REFUND_FAILED("payment.refund.failed"),
    CLIENT_ERROR("client.error");

    private final String code;
}
