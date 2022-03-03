package com.example.security.securitypractice.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("필터 init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        System.out.println("필터 destroy");
    }
}
