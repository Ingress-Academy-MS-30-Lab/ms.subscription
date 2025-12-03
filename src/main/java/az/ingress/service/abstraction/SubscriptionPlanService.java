package az.ingress.service.abstraction;

import az.ingress.model.request.SubscriptionPlanRequest;
import az.ingress.model.response.SubscriptionPlanResponse;

import java.util.List;


public interface SubscriptionPlanService {

    void create(SubscriptionPlanRequest request);


    List<SubscriptionPlanResponse> getAllByProduct(Long productId);


    SubscriptionPlanResponse get(Long id);


    void update(Long id, SubscriptionPlanRequest request);


    void delete(Long id);
}
