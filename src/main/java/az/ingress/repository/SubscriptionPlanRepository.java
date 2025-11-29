package az.ingress.repository;

import az.ingress.entity.SubscriptionPlan;
import az.ingress.enums.SubscriptionPeriod;
import az.ingress.enums.SubscriptionPlanStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends CrudRepository<SubscriptionPlan, Long> {

    List<SubscriptionPlan> findAllByActiveIsFalse();

    Optional<SubscriptionPlan> findByIdAndActiveIsTrue(Long id);

    boolean existsByProductIdAndPeriodAndStatusAndActiveFalse(Long productId, SubscriptionPeriod period, SubscriptionPlanStatus status);
}
