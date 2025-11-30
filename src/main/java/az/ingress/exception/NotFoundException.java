package az.ingress.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final ErrorMessage errorKey;
    private final Object[] args;

    public NotFoundException(ErrorMessage errorKey, Object... args) {
        super(errorKey.getCode());
        this.errorKey = errorKey;
        this.args = args;
    }
}
