package az.ingress.dao.repository;

import az.ingress.dao.entity.SubscriptionPlanEntity;
import az.ingress.model.enums.SubscriptionPeriod;
import az.ingress.model.enums.SubscriptionPlanStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends CrudRepository<SubscriptionPlanEntity, Long> {

    List<SubscriptionPlanEntity> findAllByStatusAndProductId(SubscriptionPlanStatus status, Long productId);

    Optional<SubscriptionPlanEntity> findByIdAndStatus(Long id, SubscriptionPlanStatus status);

    boolean existsByProductIdAndPeriodAndStatus(Long productId, SubscriptionPeriod period, SubscriptionPlanStatus status);
}
