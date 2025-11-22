package az.ingress.controller;

import az.ingress.dto.request.SubscriptionRequest;
import az.ingress.service.abstraction.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSubscription(@RequestBody SubscriptionRequest request) {
        subscriptionService.createSubscription(request);
    }

    @PostMapping("/{id}/renew")
    public void renewSubscription(@PathVariable Long id) {
        subscriptionService.renewSubscription(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancelSubscription(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean immediateRefund
    ) {
        subscriptionService.cancelSubscription(id, immediateRefund);
    }

    @PostMapping("/{id}/enable-auto-renew")
    public void enableAutoRenew(@PathVariable Long id) {
        subscriptionService.enableAutoRenew(id);
    }

    @PostMapping("/{id}/disable-auto-renew")
    public void disableAutoRenew(@PathVariable Long id) {
        subscriptionService.disableAutoRenew(id);
    }
}
