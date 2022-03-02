package com.example.security.securitypractice.oauth;

import com.example.security.securitypractice.auth.PrincipalDetails;
import com.example.security.securitypractice.model.User;
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
        System.out.println("user 정보 받아옴 :"+ oAuth2User.getAuthorities());


        //회원가입을진행해볼것
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub"); // sub
        String email = oAuth2User.getAttribute("email");
        String password = bCryptPasswordEncoder.encode("password");
        String username = provider+"_"+providerId; // google_213123123;
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
