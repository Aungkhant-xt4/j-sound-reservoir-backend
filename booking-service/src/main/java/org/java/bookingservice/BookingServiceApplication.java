package org.java.bookingservice;

import jakarta.annotation.PostConstruct;
import org.java.commonlibrary.config.FeignSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {
        "org.java.bookingservice",
        "org.java.commonlibrary"
})
@EnableFeignClients(defaultConfiguration = FeignSecurityConfig.class)
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
