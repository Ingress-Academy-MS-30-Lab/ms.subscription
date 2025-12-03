package az.ingress.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAll(Exception ex) {
        log.error("Unexpected error occurred", ex);

        return new ErrorResponse(
                INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR.getReasonPhrase()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleResourceNotFound(
            NotFoundException ex, Locale locale) {

        String message = messageSource.getMessage(ex.getErrorKey().getCode(), ex.getArgs(), locale);
        log.error("Resource not found: {}", message);

        return new ErrorResponse(NOT_FOUND.value(), message);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleBadRequest(
            BadRequestException ex, Locale locale) {

        String message = messageSource.getMessage(ex.getErrorKey().getCode(), ex.getArgs(), locale);
        log.error("Bad request: {}", message);

        return new ErrorResponse(BAD_REQUEST.value(), message);
    }
}
