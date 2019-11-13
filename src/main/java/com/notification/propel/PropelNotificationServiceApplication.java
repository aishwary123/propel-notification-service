package com.notification.propel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableRetry
@EnableTransactionManagement
@EnableJpaRepositories
public class PropelNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropelNotificationServiceApplication.class, args);
    }

}
