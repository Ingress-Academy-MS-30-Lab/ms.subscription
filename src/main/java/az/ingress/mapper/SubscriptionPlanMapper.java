package az.ingress.mapper;

import az.ingress.dao.entity.SubscriptionPlanEntity;
import az.ingress.model.enums.SubscriptionPeriod;
import az.ingress.model.request.SubscriptionPlanRequest;
import az.ingress.model.response.SubscriptionPlanResponse;

import static az.ingress.model.enums.SubscriptionPlanStatus.ACTIVE;

public enum SubscriptionPlanMapper {
    SUBSCRIPTION_PLAN_MAPPER;

    public SubscriptionPlanResponse toResponse(SubscriptionPlanEntity plan) {
        return SubscriptionPlanResponse.builder()
                .id(plan.getId())
                .price(plan.getPrice())
                .productId(plan.getProductId())
                .description(plan.getDescription())
                .period(plan.getPeriod().toString())
                .build();
    }

    public SubscriptionPlanEntity toEntity(SubscriptionPlanRequest request) {
        return SubscriptionPlanEntity.builder()
                .period(SubscriptionPeriod.valueOf(request.getPeriod()))
                .description(request.getDescription())
                .productId(request.getProductId())
                .price(request.getPrice())
                .title(request.getTitle())
                .status(ACTIVE)
                .build();
    }
}
