package com.example.security.securitypractice.controller;

import com.example.security.securitypractice.model.User;
import com.example.security.securitypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"","/"})
    public String index(){

        // mustache
        return "index";
    }

    // 로그인 한사람만 보게 하고 싶음
    @GetMapping("/user")
    public String user(){
        return "user";
    }

    //admin만 하ㅗ싶음
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
    @GetMapping("/member")
    public String member(){
        return "member";
    }

    //매니저만 하고싶음
    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    // 시큐리티가 해당 주소 낚아채버리고있음
    @GetMapping("/login-form")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/join-form")
    public String joinFrom(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 비밀번호 encoded 안하면 시큐리티 로그인 할 수 없음
        return "redirect:/login-form";
    }

}


