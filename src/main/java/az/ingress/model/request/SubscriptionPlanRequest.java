package az.ingress.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanRequest {
    private Long productId;
    private String period;
    private BigDecimal price;
    private String title;
    private String description;
}
