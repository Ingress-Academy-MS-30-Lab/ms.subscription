package az.ingress.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Constants {
    public static final String GLOBAL_LOG_TRACE_ID = "global-log-trace-id";

    @NoArgsConstructor(access = PRIVATE)
    public static class Headers {
        public static final String USER_ID = "X-User-Id";
    }
}
