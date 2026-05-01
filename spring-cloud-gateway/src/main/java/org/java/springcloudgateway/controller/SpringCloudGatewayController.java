package org.java.springcloudgateway.controller;

import org.java.springcloudgateway.service.SpringCloudGatewayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gateway")
public class SpringCloudGatewayController {
    private final SpringCloudGatewayService service;

    public SpringCloudGatewayController(SpringCloudGatewayService service) {
        this.service = service;
    }

    @GetMapping("/info")
    public List<String> info() {
        return service.getInfo();
    }
}