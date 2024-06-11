package com.boruomi.common.security.endpoint;


import com.alibaba.fastjson.JSONArray;
import com.boruomi.business.model.vo.SysUserVO;
import com.boruomi.business.service.Impl.SysUserService;
import com.boruomi.common.security.auth.jwt.extractor.TokenExtractor;
import com.boruomi.common.security.auth.jwt.verifier.TokenVerifier;
import com.boruomi.common.security.config.WebSecurityConfig;
import com.boruomi.common.security.exceptions.InvalidJwtToken;
import com.boruomi.common.security.model.UserContext;
import com.boruomi.common.security.model.token.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RefreshTokenEndpoint
 * 
 * @author vladimir.stankovic
 *
 * Aug 17, 2016
 */
@RestController
public class RefreshTokenEndpoint {
    @Autowired private JwtTokenFactory tokenFactory;
    @Autowired private JwtSettings jwtSettings;
    @Autowired private SysUserService userService;
    @Autowired private TokenVerifier tokenVerifier;
    @Autowired @Qualifier("jwtHeaderTokenExtractor") private TokenExtractor tokenExtractor;
    
    @RequestMapping(value="/api/auth/token", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));
        
        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String subject = refreshToken.getSubject();
        SysUserVO user = userService.getByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));
        List<String> roles = JSONArray.parseArray(user.getRoles(), String.class);
        if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
        List<GrantedAuthority> authorities = roles.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        UserContext userContext = UserContext.create(user.getUserName(), authorities);

        return tokenFactory.createAccessJwtToken(userContext);
    }
}
