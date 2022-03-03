package com.example.security.securitypractice.config;

import com.example.security.securitypractice.filter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class FilterConfig {


    public FilterRegistrationBean<MyFilter> myFilter(){
        FilterRegistrationBean<MyFilter> bean = new FilterRegistrationBean<>(new MyFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 번호가 낮은 순으로 가장 먼저 실행됨
        return bean;
    }
}
