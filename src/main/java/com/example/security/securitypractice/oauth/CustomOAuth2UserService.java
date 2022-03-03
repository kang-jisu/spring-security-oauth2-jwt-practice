package com.example.security.securitypractice.oauth;

import com.example.security.securitypractice.auth.PrincipalDetails;
import com.example.security.securitypractice.model.ProviderType;
import com.example.security.securitypractice.model.User;
import com.example.security.securitypractice.oauth.provider.OAuth2UserInfo;
import com.example.security.securitypractice.oauth.provider.OAuth2UserInfoFactory;
import com.example.security.securitypractice.repository.UserRepository;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("로그인 요청 : attributes {}", oAuth2User.getAttributes());

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2user) {

        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userinfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, oAuth2user.getAttributes());

        if (StringUtils.isBlank(userinfo.getEmail())) {
            throw new OAuth2AuthenticationException("email not found");
        }

        Optional<User> userOptional = userRepository.findByEmail(userinfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (providerType != user.getProvider()) {
                throw new IllegalArgumentException("가입에 사용한 서비스와 로그인한 서비스가 같지 않습니다. ");
            }
            updateUser(user, userinfo);
        } else {
            user = createUser(userinfo, providerType);
        }
//        log.info("토큰 생성: {}", jwtTokenUtil.generateToken(userinfo.getEmail()));

        return new PrincipalDetails(user, oAuth2user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        User user = User.builder()
                .username(userInfo.getName())
                .role("ROLE_USER")
                .password("")
                .email(userInfo.getEmail())
                .provider(providerType)
                .providerId(userInfo.getProviderId())
                .lastLoginTime(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        user.setLastLoginTime(LocalDateTime.now());
        return userRepository.save(user);
    }
}
