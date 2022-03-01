package com.example.security.securitypractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(지금 작성하는 클래스) 가 스프링 필터체인에 등록이됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 이 파일 생성하면 기본 login 작동안함


    // 해당 메서드의 리턴되는 오브젝트를 IOC로 등록해줌
    @Bean
    public BCryptPasswordEncoder encodedPwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").hasAnyRole("ADMIN","MANAGER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login-form")
                .loginProcessingUrl("/login") // /login 주소가 호출이되면 시큐리티가 낚아채서 대신 로그인을 진행해준다. 컨트롤러 안만들어도됨
                .defaultSuccessUrl("/");
    }
}
