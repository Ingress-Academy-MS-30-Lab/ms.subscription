package az.ingress.service.abstraction;

import az.ingress.dto.request.SubscriptionRequest;

public interface SubscriptionService {

    void createSubscription(SubscriptionRequest request);

    void renewSubscription(Long id);

    void cancelSubscription(Long id, boolean immediateRefund);

    void enableAutoRenew(Long id);

    void disableAutoRenew(Long id);
}
