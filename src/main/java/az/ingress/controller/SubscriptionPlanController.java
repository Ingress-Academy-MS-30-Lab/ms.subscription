package az.ingress.controller;

import az.ingress.dto.request.SubscriptionPlanRequest;
import az.ingress.dto.response.SubscriptionPlanResponse;
import az.ingress.service.abstraction.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody SubscriptionPlanRequest request) {
        subscriptionPlanService.create(request);
    }

    @GetMapping
    public List<SubscriptionPlanResponse> getAll() {
        return subscriptionPlanService.getAll();
    }

    @GetMapping("/{id}")
    public SubscriptionPlanResponse get(@PathVariable Long id) {
        return subscriptionPlanService.get(id);
    }

    @PutMapping("/{id}")
    public SubscriptionPlanResponse update(@PathVariable Long id,
                                           @RequestBody SubscriptionPlanRequest request) {
        return subscriptionPlanService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        subscriptionPlanService.delete(id);
    }
}
