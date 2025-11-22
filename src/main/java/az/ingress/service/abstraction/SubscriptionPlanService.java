package az.ingress.service.abstraction;

import az.ingress.dto.request.SubscriptionPlanRequest;
import az.ingress.dto.response.SubscriptionPlanResponse;

import java.util.List;


public interface SubscriptionPlanService {

    void create(SubscriptionPlanRequest request);


    List<SubscriptionPlanResponse> getAll();


    SubscriptionPlanResponse get(Long id);


    SubscriptionPlanResponse update(Long id, SubscriptionPlanRequest request);


    void delete(Long id);
}
