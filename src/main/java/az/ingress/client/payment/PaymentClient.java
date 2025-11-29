package az.ingress.client.payment;

import az.ingress.dto.request.PaymentRequestDto;
import az.ingress.dto.request.RefundRequestDto;
import az.ingress.dto.response.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ms.payment",
        url = "${client.payment.url}",
        path = "/internal",
        configuration = PaymentClientConfig.class
)
public interface PaymentClient {

    @PostMapping("/v1/payments")
    PaymentResponseDto pay(@RequestBody PaymentRequestDto request);

    @PostMapping("/v1/payments/refund")
    PaymentResponseDto refund(@RequestBody RefundRequestDto request);

}
