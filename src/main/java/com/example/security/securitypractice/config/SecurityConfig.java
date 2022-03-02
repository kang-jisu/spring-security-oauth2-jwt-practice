package com.example.security.securitypractice.config;

import com.example.security.securitypractice.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 1. 코드 받기(인증) 2. 엑세스토큰(권한)
// 3. 사용자 프로필 정보를 가져옴
// 4-1. 그 정보를 토대로 히원가입을 자동으로 진행시킴
// 4-2. 이메일, 전화번호, 이름 , 아이디 , 등 추가적인 정보가 필요하다면 더 추가해야함
// 이 파일 생성하면 기본 login 작동안함
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(지금 작성하는 클래스) 가 스프링 필터체인에 등록이됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    PrincipalOauth2UserService principalOauth2UserService;


    // 해당 메서드의 리턴되는 오브젝트를 IOC로 등록해줌
    @Bean
    public BCryptPasswordEncoder encodedPwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login-form")
                .loginProcessingUrl("/login") // /login 주소가 호출이되면 시큐리티가 낚아채서 대신 로그인을 진행해준다. 컨트롤러 안만들어도됨
                .defaultSuccessUrl("/"); // 로그인 성공하면 이 페이지로 보내줌

        // oauth
        http.oauth2Login()
                .loginPage("/login-form") // 로그인 페이지 url을 수동으로 변경해줌
                .userInfoEndpoint()
                .userService(principalOauth2UserService);// 그리고 구글 로그인이 완료된 후 후처리 필요 -> Tip 구글로그인이 완료되면 코드가 아닌 액세스 토큰 + 사용자 프로필 정보를 한번에 받음 (oauth가 인증정보 받아오는단계까지 다 해줌)
    }
}
