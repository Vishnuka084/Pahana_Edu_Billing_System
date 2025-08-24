package com.pahanaedu.billingsystem.httpfilters;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-17
 * Time:13:18
 */
@WebFilter(urlPatterns = "/*")
public class CORSFilter implements Filter {

    // allow exactly the dev origins you use (127.0.0.1 != localhost)
    private static final List<String> ALLOWED = Arrays.asList(
            "http://127.0.0.1:5500",
            "http://127.0.0.1:3000",
            "http://localhost:5500",
            "http://localhost:3000"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");
        if (origin != null && ALLOWED.contains(origin)) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Vary", "Origin");
            // set to true ONLY if you plan to send cookies/Authorization and use fetch(..., {credentials:'include'})
            res.setHeader("Access-Control-Allow-Credentials", "true");

            res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            // echo requested headers or provide a list
            String reqHeaders = req.getHeader("Access-Control-Request-Headers");
            res.setHeader("Access-Control-Allow-Headers",
                    (reqHeaders != null && !reqHeaders.isEmpty())
                            ? reqHeaders
                            : "Content-Type, Authorization, X-Requested-With");
            res.setHeader("Access-Control-Max-Age", "86400"); // cache preflight 24h
        }

        // Short-circuit preflight
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}