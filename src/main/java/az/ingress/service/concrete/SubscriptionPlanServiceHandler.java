package az.ingress.service.concrete;

import az.ingress.client.product.ProductClient;
import az.ingress.dto.request.SubscriptionPlanRequest;
import az.ingress.dto.response.SubscriptionPlanResponse;
import az.ingress.entity.SubscriptionPlan;
import az.ingress.enums.SubscriptionPeriod;
import az.ingress.enums.SubscriptionPlanStatus;
import az.ingress.exception.BadRequestException;
import az.ingress.exception.ResourceNotFoundException;
import az.ingress.mapper.SubscriptionPlanMapper;
import az.ingress.repository.SubscriptionPlanRepository;
import az.ingress.service.abstraction.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.ingress.exception.ErrorMessageKey.PRODUCT_NOT_FOUND;
import static az.ingress.exception.ErrorMessageKey.SUBSCRIPTION_PLAN_EXISTS;
import static az.ingress.exception.ErrorMessageKey.SUBSCRIPTION_PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceHandler implements SubscriptionPlanService {
    private final SubscriptionPlanRepository repository;
    private final ProductClient productClient;

    @Override
    public void create(SubscriptionPlanRequest request) {
        if (!productClient.existsById(request.getProductId()).isExists()) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND, request.getProductId());
        }

        if (repository.existsByProductIdAndPeriodAndStatusAndActiveFalse(
                request.getProductId(),
                SubscriptionPeriod.valueOf(request.getPeriod()),
                SubscriptionPlanStatus.ACTIVE
        )) {
            throw new BadRequestException(SUBSCRIPTION_PLAN_EXISTS, request.getProductId());
        }

        SubscriptionPlan plan = SubscriptionPlanMapper.SUBSCRIPTION_PLAN_MAPPER.toEntity(request);

        repository.save(plan);
    }

    @Override
    public List<SubscriptionPlanResponse> getAll() {
        return repository.findAllByActiveIsFalse().stream()
                .map(SubscriptionPlanMapper.SUBSCRIPTION_PLAN_MAPPER::toResponse)
                .toList();
    }

    @Override
    public SubscriptionPlanResponse get(Long id) {
        SubscriptionPlan plan = getSubscriptionPlan(id);

        return SubscriptionPlanMapper.SUBSCRIPTION_PLAN_MAPPER.toResponse(plan);
    }

    @Override
    public SubscriptionPlanResponse update(Long id, SubscriptionPlanRequest request) {
        SubscriptionPlan plan = getSubscriptionPlan(id);

        plan.setProductId(request.getProductId());
        plan.setPeriod(SubscriptionPeriod.valueOf(request.getPeriod()));
        plan.setPrice(request.getPrice());
        plan.setDescription(request.getDescription());
        plan.setStatus(SubscriptionPlanStatus.valueOf(request.getStatus()));

        repository.save(plan);

        return SubscriptionPlanMapper.SUBSCRIPTION_PLAN_MAPPER.toResponse(plan);
    }

    @Override
    public void delete(Long id) {
        SubscriptionPlan plan = getSubscriptionPlan(id);

        plan.setActive(true);
        repository.save(plan);
    }

    private SubscriptionPlan getSubscriptionPlan(Long id) {
        return repository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUBSCRIPTION_PLAN_NOT_FOUND, id));
    }
}
