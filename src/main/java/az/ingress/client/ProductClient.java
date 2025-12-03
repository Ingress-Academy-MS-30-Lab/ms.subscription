package az.ingress.client;

import az.ingress.client.decoder.CustomErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms.product",
        url = "${client.product.url}",
        path = "/internal/products",
        configuration = CustomErrorDecoder.class
)
@Profile("!local")
public interface ProductClient {

    @GetMapping("/{id}")
    void findById(@PathVariable Long id);
}
