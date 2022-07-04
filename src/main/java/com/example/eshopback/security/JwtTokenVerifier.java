package com.example.eshopback.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final SecretKey secretKey;
    private final String authorizationHeader;
    private final String tokenPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request
                .getHeader(this.authorizationHeader);

        if (Strings.isNullOrEmpty(authorizationHeader) ||
                !authorizationHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request,response);
            return;
        }

        String token = authorizationHeader.replace(tokenPrefix, "");

        try {

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            List<Map<String, String>> authorities =
                    (List<Map<String,String>> ) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            ResponseStatusException responseStatusException = new ResponseStatusException(
                    BAD_REQUEST,
                    String.format("Token %s cannot be trusted", token));
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream()
                    .println("{ \"error\": \"" + responseStatusException.getMessage() + "\" }");
        }

        filterChain.doFilter(request,response);
    }
}
