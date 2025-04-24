package com.comcast.stringinator.config;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import com.comcast.stringinator.utils.RateLimiterFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        return RateLimiterRegistry.ofDefaults();
    }

    @Bean
    public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilter(RateLimiterRegistry rateLimiterRegistry) {
        FilterRegistrationBean<RateLimiterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimiterFilter(rateLimiterRegistry));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}


