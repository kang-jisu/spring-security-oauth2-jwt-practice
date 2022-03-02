package com.example.security.securitypractice.auth;


import com.example.security.securitypractice.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어준다 .(Security ContextHolder)
// 이 세션에 들어갈 수 있는 정보 오브젝트 타입이 정해져있음 -> Authentiation 타입 객체
// Authentication 안에는 User 정보가 있어야함.
// User 오브젝트 타입도 정해져있음 -> UserDetails 타입 객체
// Security Session => Authentication => UserDetails => 이걸 우리가 구현하기위해서 만든거 PrincipalDetails

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //oauth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }
    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사이트에서 1년동안 로그인 안한 계정 휴먼 계정 하기로 했을 때
    // loginDate를 가져와서 현재시간이랑 비교해서 false 해줄 수 있을 것
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null; // 중요하지않음. attributes.get("sub")
    }
}
