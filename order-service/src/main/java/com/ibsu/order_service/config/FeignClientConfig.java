package com.ibsu.order_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig implements RequestInterceptor {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String ROLE_HEADER = "X-Role";

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = getCurrentRequestToken(attributes);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = request.getHeader(USER_ID_HEADER);
        String role = request.getHeader(ROLE_HEADER);
        if (userId != null) {
            template.header(USER_ID_HEADER, userId);
        }
        if (role != null) {
            template.header(ROLE_HEADER, role);
        }
        if (token != null) {
            template.header("Authorization", "Bearer " + token);
        }
        System.out.println(token);
    }

    private String getCurrentRequestToken(ServletRequestAttributes attributes) {
        if (attributes != null) {
            String authHeader = attributes.getRequest().getHeader("Authorization");
            return authHeader != null ? authHeader.replace("Bearer ", "") : null;
        }
        return null;
    }
}

