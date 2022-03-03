package com.example.security.securitypractice.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// login 요청해서 USERname, password 전송하면 이 피러가 동작하는데 우리는 FORMlogin disable해놔서 작동안함. 이거를 security.addfilter 해줘야함
@RequiredArgsConstructor
public class jwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // 로그인 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("jwtAuthenticationFilter:" + "로그인 시도중" );

        //1.username password 받아서
        //2. 정상인지 로그인시도 함 authenticationManager로 로그인하면 PrincipalDetailService.loadUserByUserName이 호출되는것

        //3. principalDetails를 세션에 담고->굳이 세션에 담는 이유는 권한때문 jwt토큰을 만들어서 응답해주면됨.
        return super.attemptAuthentication(request, response);
    }
}
