package com.example.demo.filter;

import com.example.demo.service.VisitCounterService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class VisitCountingFilter implements Filter {
    private final VisitCounterService visitCounterService;
    private final Set<String> excludedUrls = Set.of("/api/visits/count", "/api/visits/all");

    @Autowired
    public VisitCountingFilter(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = httpRequest.getRequestURI();

        if (!excludedUrls.contains(url)) {
            visitCounterService.incrementCounter(url);
        }

        chain.doFilter(request, response);
    }
}