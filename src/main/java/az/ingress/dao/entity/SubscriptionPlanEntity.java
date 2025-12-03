package az.ingress.dao.entity;

import az.ingress.model.enums.SubscriptionPeriod;
import az.ingress.model.enums.SubscriptionPlanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "subscription_plans")
@Entity
public class SubscriptionPlanEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long productId;

    @Enumerated(STRING)
    private SubscriptionPeriod period;

    private BigDecimal price;

    private String title;

    private String description;

    @Enumerated(STRING)
    private SubscriptionPlanStatus status;

    @OneToMany(mappedBy = "plan")
    private List<SubscriptionEntity> subscriptions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
