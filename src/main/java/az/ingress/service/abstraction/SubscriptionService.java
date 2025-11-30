package az.ingress.service.abstraction;

import az.ingress.model.request.SubscriptionRequest;

public interface SubscriptionService {

    void createSubscription(String supplierId, SubscriptionRequest request);

    void cancelSubscription(String supplierId, Long id);

    void updateAutoRenew(String supplierId, Long id, boolean enabled);
}
