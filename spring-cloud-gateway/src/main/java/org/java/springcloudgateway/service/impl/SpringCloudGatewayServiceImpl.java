package org.java.springcloudgateway.service.impl;

import org.java.springcloudgateway.service.SpringCloudGatewayService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SpringCloudGatewayServiceImpl implements SpringCloudGatewayService {
    @Override
    public List<String> getInfo() {
        return Collections.emptyList();
    }
}