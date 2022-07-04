package com.example.eshopback.config;

import com.example.eshopback.security.JwtTokenVerifier;
import com.example.eshopback.security.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.eshopback.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final SecretKey secretKey;

    @Value("${application.jwt.tokenPrefix}")
    private String tokenPrefix;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),objectMapper, tokenService))
                .addFilterAfter(new JwtTokenVerifier(secretKey, HttpHeaders.AUTHORIZATION, tokenPrefix),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
//                .antMatchers("/api/v1/registration/**").permitAll()
                .anyRequest()
                .authenticated();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }
}
