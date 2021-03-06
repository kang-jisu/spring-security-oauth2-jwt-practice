package com.example.security.securitypractice.auth;

import com.example.security.securitypractice.model.User;
import com.example.security.securitypractice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
// 시큐리티 설정에서 loginProcessingUrl("/login") 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어있는 loadUserByUserName 함수가 실행됨
@Service
public class PrincipalDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    // 파라미터 username이 아니게되면 securityConfig에서 usernameParameter 변경해주어야함.
    // 시큐리티 세션 = Authenticaion = UserDetails
    // 이게 리턴되면 이 값이 Authentication 내부에 들어가고 세션에 Authentication이 들어가는 형태가됨.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity!=null) {
            log.info("로그인 성공 : {}", userEntity);
            return new PrincipalDetails(userEntity);
        }

        return null;
    }

    public UserDetails loadUserById(Long id) {
        User userEntity = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException());
        if(userEntity!=null) {
            log.info("등록된 유저 토큰트로 로그인 : {}", userEntity);
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
