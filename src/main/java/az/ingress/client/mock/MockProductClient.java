package az.ingress.client.mock;

import az.ingress.client.ProductClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("local")
public class MockProductClient implements ProductClient {

    @Override
    public void findById(Long id) {
        log.warn("[MOCK PRODUCT CLIENT] productId={} found (mocked)", id);
    }
}
