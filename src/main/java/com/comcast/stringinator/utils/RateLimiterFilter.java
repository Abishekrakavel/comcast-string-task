package com.comcast.stringinator.utils;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    private final RateLimiterRegistry rateLimiterRegistry;

    // Constructor injection of RateLimiterRegistry
    public RateLimiterFilter(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();  // Get the client's IP address

        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ZERO)
                .build();

        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(clientIp, config);
        // Get or create a rate limiter instance for the specific IP
//        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(clientIp);

        // Check if permission is granted to the IP for the request
        boolean permissionGranted = rateLimiter.acquirePermission();  // getPermission() returns true if permission is granted

        if (permissionGranted) {
            filterChain.doFilter(request, response);  // Proceed with the request
        } else {
            response.setStatus(429);  // Too Many Requests
            response.getWriter().write("Rate limit exceeded for IP: " + clientIp);
        }
    }
}
