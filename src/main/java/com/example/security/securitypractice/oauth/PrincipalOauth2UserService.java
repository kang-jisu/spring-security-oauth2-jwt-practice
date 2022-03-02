package com.example.security.securitypractice.oauth;

import com.example.security.securitypractice.auth.PrincipalDetails;
import com.example.security.securitypractice.model.User;
import com.example.security.securitypractice.oauth.provider.GoogleUserInfo;
import com.example.security.securitypractice.oauth.provider.KakaoUserInfo;
import com.example.security.securitypractice.oauth.provider.NaverUserInfo;
import com.example.security.securitypractice.oauth.provider.OAuth2UserInfo;
import com.example.security.securitypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService  {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    // 구글로부터 바등ㄴ UserRequest 데이터에 대한 후처리 되는 함수
    /*
- 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴 (OAuth Client 라이브러리가 해줌) -> AccessToken요청
- userRequest정보 -> loadUser함수 호출 -> 회원 프로필 받음

즉 userRequest의 역할은 구글로부터 회원 프로필을 받는 요청을 생성해주는 클래스
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("로그인 요청 ============");
        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if("google".equals(registrationId)) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if("naver".equals(registrationId)){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttribute("response"));
        }
        else if("kakao".equals(registrationId)){
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }
        else throw new IllegalArgumentException();

        System.out.println("user 정보 받아옴 :"+ oAuth2User.getAuthorities());


        //회원가입을진행해볼것
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String password = bCryptPasswordEncoder.encode("password");
        String username = oAuth2UserInfo.getName();
        String role = "ROLE_USER";
        User userEntity = userRepository.findByUsername(username);

        // 가입되지 않은 회원
        if(userEntity==null ) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        // OAuthUser-> 즉 PrincipalDetails 타입으로 변경해서 리턴
        // 이게 Authentication 객체에 들어가게됨
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
