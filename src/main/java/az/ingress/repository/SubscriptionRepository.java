package az.ingress.repository;

import az.ingress.entity.Subscription;
import az.ingress.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("""
                select case when count(s) > 0 then true else false end
                from Subscription s
                join s.plan sp
                where s.supplierId = :supplierId
                  and sp.productId = :productId
            """)
    boolean existsBySupplierIdAndProductId(Long supplierId, Long productId);

    Optional<Subscription> findByIdAndStatus(Long id, SubscriptionStatus status);

    @Query("""
                select s
                from Subscription s
                join fetch s.plan
                where s.autoRenew = true
                  and s.status = 'ACTIVE'
                  and s.retryCount < 3
                  and s.endDate between :from and :to
            """)
    List<Subscription> findAllAutoRenewableInRange(LocalDateTime from,
                                                   LocalDateTime to);
}
