package com.example.security.securitypractice.controller;

import com.example.security.securitypractice.auth.PrincipalDetails;
import com.example.security.securitypractice.model.User;
import com.example.security.securitypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/info/login-user")
    public @ResponseBody
    String infoLoginUser(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal PrincipalDetails principal) {

        //authentication.getPrincipal == userDetails(기본값) == PrincipalDetails (커스텀)
        // 일반 로그인 확인
        System.out.println("/test/login ==============");
        System.out.println(authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println(principalDetails.getUser()); // principalDetails의 생성자 User를 DI로 넣어줌
        System.out.println("/test/login end==============");


        System.out.println(userDetails.getUsername());
        System.out.println(principal.getUser());

        return "세션정보확인하기";
    }

    @GetMapping("/info/oauth-login-user")
    public @ResponseBody
    String infoOAuthLoginUser(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth) {
        // service.loadUser에서 super(userRequest)한거랑 같은 값
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println(oAuth2User.getAttributes());
        System.out.println(oAuth2User.getName());
        System.out.println("==============");

        // 같음
        System.out.println("oAuth2User:" + oAuth.getAttributes());
        return "oauth 세션 정보 확인 ";
    }


    public @ResponseBody
    String loginTest(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal PrincipalDetails principal) {

        //authentication.getPrincipal == userDetails(기본값) == PrincipalDetails (커스텀)
        // 일반 로그인 확인
        System.out.println("/test/login ==============");
        System.out.println(authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println(principalDetails.getUser()); // principalDetails의 생성자 User를 DI로 넣어줌
        System.out.println("/test/login end==============");


        System.out.println(userDetails.getUsername());
        System.out.println(principal.getUser());

        return "세션정보확인하기";
    }

    @GetMapping({"", "/"})
    public String index() {

        // mustache
        return "index";
    }

    // 로그인 한사람만 보게 하고 싶음
    @GetMapping("/user")
    public @ResponseBody
    String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println(principalDetails.getUser());
        return "user";
    }

    //admin만 하ㅗ싶음
    @GetMapping("/admin")
    public @ResponseBody
    String admin() {
        return "admin";
    }

    @GetMapping("/member")
    public String member() {
        return "member";
    }

    //매니저만 하고싶음
    @GetMapping("/manager")
    public @ResponseBody
    String manager() {
        return "manager";
    }

    // 시큐리티가 해당 주소 낚아채버리고있음
//    @GetMapping("/login-form")
    public String loginForm() {
        return "loginForm";
    }

    //    @GetMapping("/join-form")
    public String joinFrom() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 비밀번호 encoded 안하면 시큐리티 로그인 할 수 없음
        return "redirect:/login-form";
    }

    // @EnableGlobalMethodSecurity(securedEnabled = true)
    @Secured("ROLE_ADMIN")// 특정 메서드에 간단하게 역할 걸어주는건ㅅ
    @GetMapping("/info")
    public @ResponseBody
    String info() {
        return "개인정보";
    }

    //보통 글로벌로 거는데 특정한거만 걸고싶을때
    // @EnableGlobalMethodSecurity(prePostEnable = true)
    @PreAuthorize("hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')") //여러개 걸때
    // @PostAuthorize() -> 메서드가 종료되고 나서 확인할 때
    @GetMapping("/data")
    public @ResponseBody
    String data() {
        return "개인정보";
    }

}


