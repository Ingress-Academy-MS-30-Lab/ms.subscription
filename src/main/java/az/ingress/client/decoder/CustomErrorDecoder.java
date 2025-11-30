package az.ingress.client.decoder;

import az.ingress.exception.CustomFeignException;
import az.ingress.logger.ApplicationLogger;
import com.fasterxml.jackson.databind.JsonNode;
import feign.Response;
import feign.codec.ErrorDecoder;

import static az.ingress.client.decoder.JsonNodeFieldName.MESSAGE;
import static az.ingress.util.MapperUtil.MAPPER_UTIL;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ApplicationLogger log = ApplicationLogger.getLogger(CustomErrorDecoder.class);

    private static final String CLIENT_ERROR = "Error occurred while processing the request";

    public Exception decode(String methodKey, Response response) {
        var errorMessage = CLIENT_ERROR;
        var statusCode = response.status();

        log.error("ActionLog.decode.error from url {} with statusCode {} ", response.request().url(), statusCode);

        try (var body = response.body().asInputStream()) {
            var jsonNode = MAPPER_UTIL.map(body, JsonNode.class);

            if (jsonNode.has(MESSAGE.getValue()))
                errorMessage = jsonNode.get(MESSAGE.getValue()).asText();

            throw new CustomFeignException(errorMessage, statusCode);
        } catch (Exception ex) {
            log.error("ActionLog.decoder.error ", ex);
            throw new CustomFeignException(errorMessage, statusCode);
        }
    }
}
