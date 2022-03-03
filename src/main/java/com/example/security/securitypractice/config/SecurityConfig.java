package com.example.security.securitypractice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(지금 작성하는 클래스) 가 스프링 필터체인에 등록이됨
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 로그인 사용하지 않을 것
                .and()
                .addFilter(corsFilter)
                .formLogin().disable() // form태그이용한 로그인방식 사용하지 않음
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .antMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll();
    }
}
