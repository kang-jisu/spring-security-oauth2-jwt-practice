package com.example.security.securitypractice.auth;


// 시큐리티가 login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어준다 .(Security ContextHolder)
// 이 세션에 들어갈 수 있는 정보 오브젝트 타입이 정해져있음 -> Authentiation 타입 객체
// Authentication 안에는 User 정보가 있어야함.
// User 오브젝트 타입도 정해져있음 -> UserDetails 타입 객체
// Security Session => Authentication => UserDetails => 이걸 우리가 구현하기위해서 만든거
public class PrincipalDetails {
}
