package com.christian.nimbo.config;

import com.christian.nimbo.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public AuthFilter(
            AuthService authService) {

        this.authService =
                authService;
    }

    //region Public paths
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String path =
                request.getRequestURI();

        return path.equals("/api/auth/login") ||
                path.equals("/api/auth/register") ||
                path.equals("/api/health") ||
                path.equals("/api/sheets/test") ||
                request.getMethod().equalsIgnoreCase("OPTIONS");
    }
    //endregion

    //region Filter
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization =
                request.getHeader("Authorization");

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        String accessToken =
                authorization.substring(7);

        if (accessToken.isBlank()) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        try {

            Map<String, Object> user =
                    authService.validateToken(
                            accessToken
                    );

            String userId =
                    (String) user.get("id");

            request.setAttribute(
                    "userId",
                    userId
            );

            filterChain.doFilter(
                    request,
                    response
            );

        } catch (Exception e) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );
        }
    }
    //endregion
}