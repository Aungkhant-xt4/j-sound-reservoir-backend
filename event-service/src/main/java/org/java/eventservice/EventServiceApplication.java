package org.java.eventservice;

import jakarta.annotation.PostConstruct;
import org.java.commonlibrary.config.FeignSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {
        "org.java.eventservice",
        "org.java.commonlibrary"
})
@EnableFeignClients(defaultConfiguration = FeignSecurityConfig.class)
public class EventServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
