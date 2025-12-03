package az.ingress.dao.entity;

import az.ingress.model.enums.SubscriptionStatus;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "subscriptions")
@Entity
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String supplierId;

    @Enumerated(STRING)
    private SubscriptionStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String transactionId;

    private Long cardId;

    private Boolean autoRenew;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "plan_id")
    private SubscriptionPlanEntity plan;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
