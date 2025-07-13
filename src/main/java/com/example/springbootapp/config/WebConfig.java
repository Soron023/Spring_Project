package com.example.springbootapp.config;

import com.example.springbootapp.interceptor.AuditInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuditInterceptor auditInterceptor;

    public WebConfig(AuditInterceptor auditInterceptor) {
        this.auditInterceptor = auditInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditInterceptor)
                .addPathPatterns("/api/**")  // Apply to all API endpoints
                .excludePathPatterns(
                    "/api/audit/**",         // Exclude audit endpoints to avoid infinite loops
                    "/api/auth/login",       // Exclude login endpoint (handled separately)
                    "/api/auth/register",    // Exclude register endpoint (handled separately)
                    "/actuator/**"           // Exclude actuator endpoints
                );
    }
} 