package az.ingress.client.product;

import az.ingress.dto.response.ProductExistenceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms.product",
        url = "${client.product.url}",
        path = "/internal",
        configuration = ProductClientConfig.class
)
public interface ProductClient {

    @GetMapping("/products/{id}/exists")
    ProductExistenceResponse existsById(@PathVariable Long id);

}
