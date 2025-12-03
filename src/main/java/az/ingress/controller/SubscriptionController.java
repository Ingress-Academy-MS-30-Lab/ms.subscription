package az.ingress.controller;

import az.ingress.model.request.SubscriptionRequest;
import az.ingress.service.abstraction.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static az.ingress.constant.Constants.Headers.USER_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createSubscription(@RequestHeader(USER_ID) @NotBlank String supplierId,
                                   @RequestBody @Valid SubscriptionRequest request) {
        subscriptionService.createSubscription(supplierId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void cancelSubscription(@RequestHeader(USER_ID) @NotBlank String supplierId,
                                   @PathVariable Long id) {
        subscriptionService.cancelSubscription(supplierId, id);
    }

    @PatchMapping("/{id}/auto-renew")
    @ResponseStatus(NO_CONTENT)
    public void updateAutoRenew(@RequestHeader(USER_ID) @NotBlank String supplierId,
                                @PathVariable Long id,
                                @RequestParam boolean enabled) {
        subscriptionService.updateAutoRenew(supplierId, id, enabled);
    }
}
