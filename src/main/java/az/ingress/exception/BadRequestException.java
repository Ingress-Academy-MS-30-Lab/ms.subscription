package az.ingress.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorMessageKey errorKey;
    private final Object[] args;

    public BadRequestException(ErrorMessageKey errorKey, Object... args) {
        super(errorKey.getKey());
        this.errorKey = errorKey;
        this.args = args;
    }
}
