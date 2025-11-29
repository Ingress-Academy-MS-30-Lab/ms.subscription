package az.ingress.mapper;

import az.ingress.dto.request.SubscriptionPlanRequest;
import az.ingress.dto.response.SubscriptionPlanResponse;
import az.ingress.entity.SubscriptionPlan;
import az.ingress.enums.SubscriptionPeriod;
import az.ingress.enums.SubscriptionPlanStatus;

public enum SubscriptionPlanMapper {
    SUBSCRIPTION_PLAN_MAPPER;

    public SubscriptionPlanResponse toResponse(SubscriptionPlan plan) {
        return SubscriptionPlanResponse.builder()
                .id(plan.getId())
                .price(plan.getPrice())
                .productId(plan.getProductId())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .description(plan.getDescription())
                .period(plan.getPeriod().toString())
                .build();
    }

    public SubscriptionPlan toEntity(SubscriptionPlanRequest request) {
        return SubscriptionPlan.builder()
                .status(SubscriptionPlanStatus.valueOf(request.getStatus()))
                .period(SubscriptionPeriod.valueOf(request.getPeriod()))
                .description(request.getDescription())
                .productId(request.getProductId())
                .price(request.getPrice())
                .title(request.getTitle())
                .build();
    }
}
