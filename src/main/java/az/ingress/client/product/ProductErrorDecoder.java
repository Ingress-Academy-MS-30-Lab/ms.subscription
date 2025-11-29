package az.ingress.client.product;

import az.ingress.exception.CustomFeignClientException;
import az.ingress.exception.ErrorMessageKey;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey,
                            Response response) {
        log.error("Feign error decoder called for methodKey: {}, response: {}", methodKey, response);

        var errorMessage = "Error occurred while calling test service";
        String errorCode = null;
        JsonNode jsonNode;

        try (var body = response.body().asInputStream()) {
            jsonNode = new ObjectMapper().readValue(body, JsonNode.class);
        } catch (Exception e) {
            throw new CustomFeignClientException(ErrorMessageKey.CLIENT_ERROR.getKey(), response.status(), errorMessage);
        }

        if (jsonNode.has("message")) {
            errorMessage = jsonNode.get("message").asText();
        }

        if (jsonNode.has("errorCode")) {
            errorCode = jsonNode.get("errorCode").asText();
        }

        log.error("Error response from test service: status {}, errorCode {}, message {}",
                response.status(), errorCode, errorMessage);

        return new CustomFeignClientException(errorCode, response.status(), errorMessage);
    }
}
