package az.ingress.enums;

import java.time.LocalDateTime;

public enum SubscriptionPeriod {

    WEEKLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime start) {
            return start.plusWeeks(1);
        }
    },

    MONTHLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime start) {
            return start.plusMonths(1);
        }
    },

    YEARLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime start) {
            return start.plusYears(1);
        }
    };

    public abstract LocalDateTime calculateEndDate(LocalDateTime start);
}
