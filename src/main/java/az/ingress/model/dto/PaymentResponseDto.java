package az.ingress.model.dto;

import az.ingress.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private PaymentStatus status;
    private String transactionId;
}
