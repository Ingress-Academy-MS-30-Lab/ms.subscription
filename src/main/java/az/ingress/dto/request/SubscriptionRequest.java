package az.ingress.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {
    private Long cardId;
    private Long supplierId;
    private Long subscriptionPlanId;
    private boolean autoRenew;
}
