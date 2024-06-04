package com.boruomi.security.config;

import com.boruomi.security.auth.ajax.AjaxAuthenticationProvider;
import com.boruomi.security.auth.ajax.AjaxLoginProcessingFilter;
import com.boruomi.security.auth.jwt.JwtAuthenticationProvider;
import com.boruomi.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.boruomi.security.auth.jwt.SkipPathRequestMatcher;
import com.boruomi.security.auth.jwt.extractor.TokenExtractor;
import com.boruomi.security.model.CustomCorsFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    public static final String API_ROOT_URL = "/api/**";

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private AjaxAuthenticationProvider ajaxAuthenticationProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(
                AUTHENTICATION_URL,
                REFRESH_TOKEN_URL,
                "/console"
        );

        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint())) // 自定义用户认证失败的处理器
                .formLogin(AbstractHttpConfigurer::disable) // 禁用默认登录页面
                .logout(AbstractHttpConfigurer::disable) // 禁用默认登出页面
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 禁用session，前后端分离不需要
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTHENTICATION_URL, REFRESH_TOKEN_URL, "/console").permitAll()
                        .anyRequest().authenticated()
                ) // 设置权限，除了登录登出不需要认证，其余均需要认证
                .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL,authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, API_ROOT_URL,authenticationManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public ProviderManager providerManager() {
        //public ProviderManager(List<AuthenticationProvider> providers)
        //每个 AuthenticationProvider 都执行一种特定类型的认证
        return new ProviderManager(List.of(ajaxAuthenticationProvider, jwtAuthenticationProvider));
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        };
    }


    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint, AuthenticationManager authenticationManager) throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern, AuthenticationManager authenticationManager) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
}
