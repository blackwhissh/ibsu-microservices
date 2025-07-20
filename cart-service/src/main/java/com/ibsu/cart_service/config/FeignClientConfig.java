package com.ibsu.cart_service.config;

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

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // Forward JWT
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                template.header("Authorization", authHeader);
                System.out.println("Forwarding token: " + authHeader);
            }

            // Forward custom headers
            String userId = request.getHeader(USER_ID_HEADER);
            String role = request.getHeader(ROLE_HEADER);

            if (userId != null) {
                template.header(USER_ID_HEADER, userId);
            }
            if (role != null) {
                template.header(ROLE_HEADER, role);
            }
        }
    }
}
