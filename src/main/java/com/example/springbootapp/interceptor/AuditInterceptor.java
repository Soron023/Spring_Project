package com.example.springbootapp.interceptor;

import com.example.springbootapp.entity.AuditLevel;
import com.example.springbootapp.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AuditInterceptor implements HandlerInterceptor {

    private final AuditService auditService;

    public AuditInterceptor(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Store start time for execution time calculation
        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute("requestId", UUID.randomUUID().toString());
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            // Get execution time
            Long startTime = (Long) request.getAttribute("startTime");
            Long executionTime = startTime != null ? System.currentTimeMillis() - startTime : null;
            
            // Get request information
            String requestUrl = request.getRequestURL().toString();
            String httpMethod = request.getMethod();
            Integer statusCode = response.getStatus();
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            String sessionId = request.getSession().getId();
            
            // Get user information
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = "ANONYMOUS";
            String username = "ANONYMOUS";
            
            if (authentication != null && authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getName())) {
                userId = authentication.getName();
                username = authentication.getName();
            }
            
            // Determine action and resource type based on request
            String action = determineAction(httpMethod, requestUrl);
            String resourceType = determineResourceType(requestUrl);
            String resourceId = extractResourceId(requestUrl);
            String resourceName = determineResourceName(requestUrl);
            
            // Determine audit level based on action and status code
            AuditLevel auditLevel = determineAuditLevel(action, statusCode);
            
            // Create audit log entry
            auditService.createAuditLog(
                userId,
                username,
                action,
                resourceType,
                resourceId,
                resourceName,
                buildAuditDetails(request, response, executionTime),
                auditLevel
            );
            
        } catch (Exception e) {
            // Log the error but don't fail the request
            System.err.println("Error in audit interceptor: " + e.getMessage());
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private String determineAction(String httpMethod, String requestUrl) {
        return switch (httpMethod.toUpperCase()) {
            case "GET" -> "READ";
            case "POST" -> "CREATE";
            case "PUT", "PATCH" -> "UPDATE";
            case "DELETE" -> "DELETE";
            default -> httpMethod.toUpperCase();
        };
    }

    private String determineResourceType(String requestUrl) {
        if (requestUrl.contains("/api/users")) return "USER";
        if (requestUrl.contains("/api/products")) return "PRODUCT";
        if (requestUrl.contains("/api/categories")) return "CATEGORY";
        if (requestUrl.contains("/api/roles")) return "ROLE";
        if (requestUrl.contains("/api/auth")) return "AUTH";
        if (requestUrl.contains("/api/audit")) return "AUDIT";
        return "UNKNOWN";
    }

    private String extractResourceId(String requestUrl) {
        // Extract ID from URL patterns like /api/users/123
        String[] parts = requestUrl.split("/");
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].matches("\\d+")) {
                return parts[i];
            }
        }
        return null;
    }

    private String determineResourceName(String requestUrl) {
        if (requestUrl.contains("/api/users")) return "User Management";
        if (requestUrl.contains("/api/products")) return "Product Management";
        if (requestUrl.contains("/api/categories")) return "Category Management";
        if (requestUrl.contains("/api/roles")) return "Role Management";
        if (requestUrl.contains("/api/auth")) return "Authentication";
        if (requestUrl.contains("/api/audit")) return "Audit Management";
        return "Unknown Resource";
    }

    private AuditLevel determineAuditLevel(String action, Integer statusCode) {
        // Security-related actions
        if ("DELETE".equals(action)) return AuditLevel.WARNING;
        
        // Error responses
        if (statusCode != null && statusCode >= 400) {
            if (statusCode >= 500) return AuditLevel.ERROR;
            if (statusCode == 401 || statusCode == 403) return AuditLevel.SECURITY;
            return AuditLevel.WARNING;
        }
        
        // Authentication actions
        if ("LOGIN_SUCCESS".equals(action) || "LOGIN_FAILED".equals(action) || "LOGOUT".equals(action)) {
            return "LOGIN_FAILED".equals(action) ? AuditLevel.SECURITY : AuditLevel.INFO;
        }
        
        return AuditLevel.INFO;
    }

    private String buildAuditDetails(HttpServletRequest request, HttpServletResponse response, Long executionTime) {
        StringBuilder details = new StringBuilder();
        details.append("Request completed");
        
        if (executionTime != null) {
            details.append(" in ").append(executionTime).append("ms");
        }
        
        details.append(" | Status: ").append(response.getStatus());
        details.append(" | Method: ").append(request.getMethod());
        details.append(" | URL: ").append(request.getRequestURL());
        
        // Add query parameters if present
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            details.append(" | Query: ").append(queryString);
        }
        
        return details.toString();
    }
} 