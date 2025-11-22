package az.ingress.client;

import az.ingress.dto.request.PaymentRequest;
import az.ingress.dto.request.RefundRequest;
import az.ingress.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-ms",
        url = "${client.payment.url}"
)
public interface PaymentClient {

    @PostMapping("/api/v1/payments")
    PaymentResponse pay(@RequestBody PaymentRequest request);

    @PostMapping("/api/v1/payments/refund")
    PaymentResponse refund(@RequestBody RefundRequest request);

}
