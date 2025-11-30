package az.ingress.controller;

import az.ingress.model.request.SubscriptionPlanRequest;
import az.ingress.model.response.SubscriptionPlanResponse;
import az.ingress.service.abstraction.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void create(@RequestBody SubscriptionPlanRequest request) {
        subscriptionPlanService.create(request);
    }

    @GetMapping
    public List<SubscriptionPlanResponse> getAllByProduct(@RequestParam Long productId) {
        return subscriptionPlanService.getAllByProduct(productId);
    }

    @GetMapping("/{id}")
    public SubscriptionPlanResponse get(@PathVariable Long id) {
        return subscriptionPlanService.get(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Long id,
                       @RequestBody SubscriptionPlanRequest request) {
        subscriptionPlanService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        subscriptionPlanService.delete(id);
    }
}
