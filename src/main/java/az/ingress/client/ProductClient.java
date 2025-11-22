package az.ingress.client;

import az.ingress.dto.response.ProductExistenceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-ms",
        url = "${client.product.url}"
)
public interface ProductClient {

    @GetMapping("/products/{id}/exists")
    ProductExistenceResponse existsById(@PathVariable Long id);
}
