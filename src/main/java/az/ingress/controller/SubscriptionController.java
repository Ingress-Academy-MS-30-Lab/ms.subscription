package az.ingress.controller;

import az.ingress.dto.request.SubscriptionRequest;
import az.ingress.service.abstraction.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createSubscription(@RequestBody @Valid SubscriptionRequest request) {
        subscriptionService.createSubscription(request);
    }

    @ResponseStatus(NO_CONTENT)
    @PostMapping("/{id}/renew")
    public void renewSubscription(@PathVariable Long id) {
        subscriptionService.renewSubscription(id);
    }

    @ResponseStatus(NO_CONTENT)
    @PostMapping("/{id}/cancel")
    public void cancelSubscription(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean immediateRefund
    ) {
        subscriptionService.cancelSubscription(id, immediateRefund);
    }

    @ResponseStatus(NO_CONTENT)
    @PostMapping("/{id}/enable-auto-renew")
    public void enableAutoRenew(@PathVariable Long id) {
        subscriptionService.enableAutoRenew(id);
    }

    @ResponseStatus(NO_CONTENT)
    @PostMapping("/{id}/disable-auto-renew")
    public void disableAutoRenew(@PathVariable Long id) {
        subscriptionService.disableAutoRenew(id);
    }
}
