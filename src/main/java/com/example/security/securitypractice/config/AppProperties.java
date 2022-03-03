package com.example.security.securitypractice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// application.yml에 있는 app.auth / app.oauth 정보를 가져옴
// redirectUri를 저장하고 가져올 수 있게 하는 역할
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Auth auth;
    private OAuth2 oauth2;

    public static class Auth {
        private String tokenSecret;
        private Long tokenExpirationMsec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public Long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public void setTokenExpirationMsec(Long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }
    }

    public static class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public void setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public void setOauth2(OAuth2 oauth2) {
        this.oauth2 = oauth2;
    }
}
