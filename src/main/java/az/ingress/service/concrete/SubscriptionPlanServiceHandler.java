package az.ingress.service.concrete;

import az.ingress.client.ProductClient;
import az.ingress.dao.entity.SubscriptionPlanEntity;
import az.ingress.dao.repository.SubscriptionPlanRepository;
import az.ingress.exception.BadRequestException;
import az.ingress.exception.NotFoundException;
import az.ingress.model.enums.SubscriptionPeriod;
import az.ingress.model.request.SubscriptionPlanRequest;
import az.ingress.model.response.SubscriptionPlanResponse;
import az.ingress.service.abstraction.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.ingress.exception.ErrorMessage.SUBSCRIPTION_PLAN_EXISTS;
import static az.ingress.exception.ErrorMessage.SUBSCRIPTION_PLAN_NOT_FOUND;
import static az.ingress.mapper.SubscriptionPlanMapper.SUBSCRIPTION_PLAN_MAPPER;
import static az.ingress.model.enums.SubscriptionPlanStatus.ACTIVE;
import static az.ingress.model.enums.SubscriptionPlanStatus.PASSIVE;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceHandler implements SubscriptionPlanService {
    private final SubscriptionPlanRepository repository;
    private final ProductClient productClient;

    @Override
    public void create(SubscriptionPlanRequest request) {
        productClient.findById(request.getProductId());

        if (repository.existsByProductIdAndPeriodAndStatus(request.getProductId(),
                SubscriptionPeriod.valueOf(request.getPeriod()),
                ACTIVE)) {
            throw new BadRequestException(SUBSCRIPTION_PLAN_EXISTS, request.getProductId());
        }

        var plan = SUBSCRIPTION_PLAN_MAPPER.toEntity(request);

        repository.save(plan);
    }

    @Override
    public List<SubscriptionPlanResponse> getAllByProduct(Long productId) {
        return repository.findAllByStatusAndProductId(ACTIVE, productId).stream()
                .map(SUBSCRIPTION_PLAN_MAPPER::toResponse)
                .toList();
    }

    @Override
    public SubscriptionPlanResponse get(Long id) {
        var plan = getSubscriptionPlan(id);

        return SUBSCRIPTION_PLAN_MAPPER.toResponse(plan);
    }

    @Override
    public void update(Long id, SubscriptionPlanRequest request) {
        var plan = getSubscriptionPlan(id);

        plan.setProductId(request.getProductId());
        plan.setPeriod(SubscriptionPeriod.valueOf(request.getPeriod()));
        plan.setPrice(request.getPrice());
        plan.setDescription(request.getDescription());

        repository.save(plan);
    }

    @Override
    public void delete(Long id) {
        var plan = getSubscriptionPlan(id);

        plan.setStatus(PASSIVE);
        repository.save(plan);
    }

    private SubscriptionPlanEntity getSubscriptionPlan(Long id) {
        return repository.findByIdAndStatus(id, ACTIVE)
                .orElseThrow(() -> new NotFoundException(SUBSCRIPTION_PLAN_NOT_FOUND, id));
    }
}
