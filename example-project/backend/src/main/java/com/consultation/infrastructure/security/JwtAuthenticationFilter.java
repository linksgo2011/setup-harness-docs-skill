package com.consultation.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            if (tokenProvider.validate(token)) {
                var claims = tokenProvider.parse(token);
                String role = claims.get("role", String.class);
                UserContextHolder.set(UserContext.builder()
                        .userId(claims.getSubject())
                        .email(claims.get("email", String.class))
                        .role(role)
                        .build());
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                claims.getSubject(), null,
                                role != null ? Collections.singletonList(new SimpleGrantedAuthority(role))
                                        : Collections.emptyList()
                        )
                );
            }
        }
        try { chain.doFilter(request, response); } finally { UserContextHolder.clear(); SecurityContextHolder.clearContext(); }
    }
}
