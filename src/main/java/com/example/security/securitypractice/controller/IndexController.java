package com.example.security.securitypractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

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
    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc(){
        return "회원가입 완료됨";
    }
}


