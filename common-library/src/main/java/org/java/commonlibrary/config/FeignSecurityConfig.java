package org.java.commonlibrary.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@ConditionalOnClass(name = "feign.RequestInterceptor")
public class FeignSecurityConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            System.out.println("Intercepted Token: " + authHeader);
            requestTemplate.header(HttpHeaders.AUTHORIZATION, authHeader);
        } else {
            System.out.println("Attributes are NULL - Thread context lost!");
        }
    }


}
