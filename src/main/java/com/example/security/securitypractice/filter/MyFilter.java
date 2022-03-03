package com.example.security.securitypractice.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("필터 init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        if("POST".equals(req.getMethod())){
            String headerAuth = req.getHeader("Authorization");
            if ("token".equals(headerAuth)){
                chain.doFilter(request,response);
            }else {
                chain.doFilter(request,response);
            }
        }
        else chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        System.out.println("필터 destroy");
    }
}
