package az.ingress;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

@EnableScheduling
@EnableFeignClients
@EnableSchedulerLock(defaultLockAtMostFor = "5m")
@SpringBootApplication
public class SubscriptionApplication {

    public static void main(String[] args) {
        run(SubscriptionApplication.class, args);
    }
}
