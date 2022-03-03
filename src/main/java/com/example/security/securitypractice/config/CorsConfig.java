package com.example.security.securitypractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {


    @Bean
    public CorsFilter CorsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        System.out.println("corsfilter");
        config.setAllowCredentials(true); // 쿠키요청허용
        config.addAllowedOriginPattern("*"); // ip 응답 허용
        config.addAllowedHeader("*"); // 모든 header 응답 허용
        config.addAllowedMethod("*"); // 모든 http method 허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
