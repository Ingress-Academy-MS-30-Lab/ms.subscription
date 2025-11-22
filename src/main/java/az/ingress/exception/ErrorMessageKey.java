package az.ingress.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessageKey {
    PRODUCT_NOT_FOUND("product.not.found"),
    SUBSCRIPTION_PLAN_NOT_FOUND("subscription.plan.not.found"),
    SUBSCRIPTION_PLAN_EXISTS("subscription.plan.exists"),
    SUBSCRIPTION_NOT_FOUND("subscription.not.found");

    private final String key;
}
