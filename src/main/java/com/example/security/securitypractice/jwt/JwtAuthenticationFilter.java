package com.example.security.securitypractice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.securitypractice.auth.PrincipalDetails;
import com.example.security.securitypractice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

@Slf4j
// login 요청해서 USERname, password 전송하면 이 피러가 동작하는데 우리는 FORMlogin disable해놔서 작동안함. 이거를 security.addfilter 해줘야함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final String secretKey = "secretKey";

    // 로그인 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("jwtAuthenticationFilter: 로그인 시도중" );

        try {
            //1.username password 받아서

            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            log.info(user.toString());

            // PrincipalDetailsService의 loadUserByUsername()이 실행됨
            //2. 정상인지 로그인시도 함 authenticationManager로 로그인하면 PrincipalDetailService.loadUserByUserName이 호출되는것
            //DB에 있는 Username과 pw가 일치한다는것.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            //3. principalDetails를 세션에 담고->굳이 세션에 담는 이유는 권한때문 jwt토큰을 만들어서 응답해주면됨.
            return authentication;
        }catch(IOException e) {
            throw new RuntimeException();
        }catch (BadCredentialsException ex){
            log.info("로그인 실패");
            throw ex;
        }
    }

    // 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨 여기서 jwt 토큰 만듦
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        log.info("인증 완료 :{}",principal);

        String jwt = JWT.create()
                .withSubject("jwt 토큰")
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*10*60*10))
                .withClaim("id",principal.getUser().getId())
                .withClaim("username", principal.getUser().getUsername())
                .sign(Algorithm.HMAC256(secretKey));
        response.addHeader("Authentication","Bearer "+jwt);
    }
}
