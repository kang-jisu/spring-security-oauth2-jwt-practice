package com.example.security.securitypractice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.security.securitypractice.auth.PrincipalDetails;
import com.example.security.securitypractice.model.User;
import com.example.security.securitypractice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 가지고있는 필터중에 BasicAuthenticationFilter -> 권한이나 인증이 필요한 특정 주소를 요청했을 때 이 필터를 거침
// 권한이나 인증이 필요한 주소가 아니면 이 필터를 안탐
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter 실행 ");
        String jwtHeader = request.getHeader("Authorization");
        log.info("jwtHeader:{}", jwtHeader);
        if(jwtHeader==null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String jwtToken = jwtHeader.replace("Bearer ","");
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256("secretKey")).build().verify(jwtToken);
            String username = decodedJWT.getClaim("username").asString();
            if(username!=null) {
                System.out.println(username);
                User userEntity = userRepository.findByUsername(username);
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                //jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                // 강제로 security 세션에 접근하여 authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
              }
        } catch (Exception e){
            System.out.println(e);
            throw e;
        }
        chain.doFilter(request, response);

    }
}
