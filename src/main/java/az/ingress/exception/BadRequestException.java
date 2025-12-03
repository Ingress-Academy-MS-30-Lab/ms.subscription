package az.ingress.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorMessage errorKey;
    private final Object[] args;

    public BadRequestException(ErrorMessage errorKey, Object... args) {
        super(errorKey.getCode());
        this.errorKey = errorKey;
        this.args = args;
    }
}
