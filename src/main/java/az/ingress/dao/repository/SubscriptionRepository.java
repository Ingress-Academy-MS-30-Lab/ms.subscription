package az.ingress.dao.repository;

import az.ingress.dao.entity.SubscriptionEntity;
import az.ingress.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    @Query("""
                select case when count(s) > 0 then true else false end
                from SubscriptionEntity s
                join s.plan sp
                where s.supplierId = :supplierId
                  and sp.productId = :productId
            """)
    boolean existsBySupplierIdAndProductId(String supplierId, Long productId);

    Optional<SubscriptionEntity> findByIdAndSupplierIdAndStatus(Long id, String supplierId, SubscriptionStatus status);

    @Query("""
            select s
            from SubscriptionEntity s
            join fetch s.plan
            where s.status in :statuses
              and s.endDate between :from and :to
            """)
    List<SubscriptionEntity> findAllAutoRenewableInRange(LocalDateTime from,
                                                         LocalDateTime to,
                                                         List<SubscriptionStatus> statuses);
}
