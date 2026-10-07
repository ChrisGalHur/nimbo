package com.christian.nimbo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthFilterConfig {

    //region Auth filter
    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration(
            AuthFilter authFilter) {

        FilterRegistrationBean<AuthFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(authFilter);
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);

        return registration;
    }
    //endregion
}