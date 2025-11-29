package az.ingress.exception;

import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {
    private final String errorCode;
    private final Integer status;
    private final String errorMessage;

    public CustomFeignClientException(String errorCode, Integer status, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
