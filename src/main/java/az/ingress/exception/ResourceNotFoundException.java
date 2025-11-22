package az.ingress.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final ErrorMessageKey errorKey;
    private final Object[] args;

    public ResourceNotFoundException(ErrorMessageKey errorKey, Object... args) {
        super(errorKey.getKey());
        this.errorKey = errorKey;
        this.args = args;
    }
}
