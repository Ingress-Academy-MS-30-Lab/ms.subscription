package az.ingress.dao.entity;

import az.ingress.model.enums.Currency;
import az.ingress.model.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "subscriptions")
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private UUID supplierId;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Instant startDate;

    private Instant endDate;

    private boolean autoRenew;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
