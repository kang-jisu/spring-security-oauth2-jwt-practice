package com.example.security.securitypractice.config;

import com.example.security.securitypractice.filter.TokenAuthenticationFilter;
import com.example.security.securitypractice.oauth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 1. 코드 받기(인증) 2. 엑세스토큰(권한)
// 3. 사용자 프로필 정보를 가져옴
// 4-1. 그 정보를 토대로 히원가입을 자동으로 진행시킴
// 4-2. 이메일, 전화번호, 이름 , 아이디 , 등 추가적인 정보가 필요하다면 더 추가해야함
// 이 파일 생성하면 기본 login 작동안함
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(지금 작성하는 클래스) 가 스프링 필터체인에 등록이됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
// secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHORIZATION_RESPONSE_BASE_URI = "/oauth2/callback/*";
    public static final String AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorize";

    private final CustomOAuth2UserService customOauth2UserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthoirzationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    // 해당 메서드의 리턴되는 오브젝트를 IOC로 등록해줌
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/",
                        "/error",
                        "favicon.ico",
                        "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js"
                )
                .permitAll()
                .antMatchers("/auth/**", "oauth2/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .disable();// 로그인 성공하면 이 페이지로 보내줌
        // oauth
        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri(AUTHORIZATION_REQUEST_BASE_URI) // 프론트엔드에서 맨 처음 로그인 요청을 보내는 URI -> 이 때 로그인된 정보가 없으면 딱 한번  RedirectURI로 provider가 리다이렉트 시킨다.
                .authorizationRequestRepository(cookieOAuth2AuthoirzationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri(AUTHORIZATION_RESPONSE_BASE_URI) // 유저가 동의하면 이 URI로 권한코드와 함께 돌아온다. 유저가 거부하면 이 URI로 error와 함께 옴
                .and()
                .userInfoEndpoint()
                .userService(customOauth2UserService)// 그리고 구글 로그인이 완료된 후 후처리 필요 -> Tip 구글로그인이 완료되면 코드가 아닌 액세스 토큰 + 사용자 프로필 정보를 한번에 받음 (oauth가 인증정보 받아오는단계까지 다 해줌)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

}
