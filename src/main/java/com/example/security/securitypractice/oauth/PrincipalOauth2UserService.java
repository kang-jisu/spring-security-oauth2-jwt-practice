package com.example.security.securitypractice.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService  {

    // 구글로부터 바등ㄴ UserRequest 데이터에 대한 후처리 되는 함수
    /*
- 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴 (OAuth Client 라이브러리가 해줌) -> AccessToken요청
- userRequest정보 -> loadUser함수 호출 -> 회원 프로필 받음

즉 userRequest의 역할은 구글로부터 회원 프로필을 받는 요청을 생성해주는 클래스
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}
