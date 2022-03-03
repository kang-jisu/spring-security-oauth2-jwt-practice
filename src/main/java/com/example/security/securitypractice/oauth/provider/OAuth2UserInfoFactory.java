package com.example.security.securitypractice.oauth.provider;

import com.example.security.securitypractice.model.ProviderType;

import java.util.Map;

// provider 타입에 따라 알맞은 UserInfo를 가져오도록 구현
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE:
                return new GoogleUserInfo(attributes);
            case KAKAO:
                return new KakaoUserInfo(attributes);
            case NAVER:
                return new NaverUserInfo(attributes);
            default:
                throw new IllegalArgumentException("Invalid Provider Type");
        }
    }
}
