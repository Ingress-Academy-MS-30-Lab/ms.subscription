package az.ingress.client;

import az.ingress.client.decoder.CustomErrorDecoder;
import az.ingress.model.dto.PaymentRequestDto;
import az.ingress.model.dto.PaymentResponseDto;
import az.ingress.model.dto.RefundRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ms.payment",
        url = "${client.payment.url}",
        path = "/internal/v1/payments",
        configuration = CustomErrorDecoder.class
)
public interface PaymentClient {

    @PostMapping
    PaymentResponseDto pay(@RequestBody PaymentRequestDto request);

    @PostMapping("/refund")
    PaymentResponseDto refund(@RequestBody RefundRequestDto request);
}
