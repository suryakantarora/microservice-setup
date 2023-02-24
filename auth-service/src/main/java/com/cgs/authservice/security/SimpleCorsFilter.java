package com.cgs.authservice.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "*");
        res.addHeader("Access-Control-Max-Age", "3600");
        res.addHeader("Access-Control-Allow-Headers", "*");
        res.addHeader("Content-Type", "application/json");
        res.addHeader("Access", "application/json");
        res.addHeader("X-FRAME-OPTIONS", "DENY");
        res.addHeader("X-Content-Type-OPTIONS", "nosniff");
        res.addHeader("X-XSS-Protection", "1; mode=block");
        res.addHeader("Pragma", "no-cache");
        res.addHeader("Cache-Control", "no-cache, no-store,max-age=0, must-revalidate");
        res.addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        res.addHeader("Expires", "0");
        res.addHeader("ProjectName", "Webportal");
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

}

