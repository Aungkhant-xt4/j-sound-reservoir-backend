package org.java.inventoryservice;

import jakarta.annotation.PostConstruct;
import org.java.commonlibrary.config.FeignSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {
        "org.java.inventoryservice",
        "org.java.commonlibrary"
})
@EnableFeignClients(defaultConfiguration = FeignSecurityConfig.class)
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
