package az.ingress.client.product;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class ProductClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ProductErrorDecoder();
    }
}
