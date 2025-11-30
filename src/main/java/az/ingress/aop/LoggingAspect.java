package az.ingress.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

import static az.ingress.constant.Constants.GLOBAL_LOG_TRACE_ID;
import static az.ingress.util.MapperUtil.MAPPER_UTIL;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
    }

    @Before("controller()")
    public void logBefore(JoinPoint joinPoint) {
        String traceId = UUID.randomUUID().toString();
        MDC.put(GLOBAL_LOG_TRACE_ID, traceId);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("REQUEST. TRACE_ID: {}, TARGET: {}.{}, METHOD: {}, URL: {}, payload = {}",
                    traceId,
                    getClassName(joinPoint),
                    getMethodName(joinPoint),
                    request.getMethod(),
                    request.getRequestURL(),
                    Arrays.toString(joinPoint.getArgs()));
        }
    }

    @AfterReturning(pointcut = "controller()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String traceId = MDC.get(GLOBAL_LOG_TRACE_ID);
        String json = null;
        if (result != null) {
            json = MAPPER_UTIL.map(result);
        }
        log.info("RESPONSE-SUCCESS. TRACE_ID: {}, TARGET: {}.{}, data: {}",
                traceId,
                getClassName(joinPoint),
                getMethodName(joinPoint),
                json);
    }

    @AfterThrowing(pointcut = "controller()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String traceId = MDC.get(GLOBAL_LOG_TRACE_ID);
        log.error("RESPONSE-EXCEPTION. TRACE_ID: {}, TARGET: {}.{}, exception-class = {}, message = {}",
                traceId, getClassName(joinPoint),
                getMethodName(joinPoint), exception.getClass(), exception.getMessage().replace("\n", "\\n"));
    }

    private String getClassName(JoinPoint joinPoint) {
        return joinPointIsNotNull(joinPoint) ? joinPoint.getSignature().getDeclaringTypeName() : null;
    }

    private String getMethodName(JoinPoint joinPoint) {
        return joinPointIsNotNull(joinPoint) ? joinPoint.getSignature().getName() : null;
    }

    private boolean joinPointIsNotNull(JoinPoint joinPoint) {
        return joinPoint != null && joinPoint.getSignature() != null;
    }
}
