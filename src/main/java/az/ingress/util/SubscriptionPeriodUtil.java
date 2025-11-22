package az.ingress.util;

import az.ingress.enums.SubscriptionPeriod;

import java.time.LocalDateTime;

public final class SubscriptionPeriodUtil {

    private SubscriptionPeriodUtil() {
    }

    public static LocalDateTime calculateEndDate(LocalDateTime start, SubscriptionPeriod period) {
        return switch (period) {
            case WEEKLY -> start.plusWeeks(1);
            case MONTHLY -> start.plusMonths(1);
            case YEARLY -> start.plusYears(1);
        };
    }
}
