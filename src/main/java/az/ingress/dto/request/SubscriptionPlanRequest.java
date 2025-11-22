package az.ingress.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPlanRequest {
    private Long productId;
    private String period;
    private BigDecimal price;
    private String title;
    private String description;
    private String status;
}
