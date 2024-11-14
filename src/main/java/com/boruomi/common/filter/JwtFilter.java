package com.boruomi.common.filter;

import com.alibaba.fastjson.JSON;
import com.boruomi.common.Const;
import com.boruomi.common.response.R;
import com.boruomi.common.response.ResultCode;
import com.boruomi.common.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter("/*")
public class JwtFilter implements Filter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 获取请求的路径
        String requestURI = httpRequest.getRequestURI();
        // 判断路径是否是免验证路径
        if (isExcludedPath(requestURI)) {
            // 如果是免验证路径，直接放行请求
            chain.doFilter(request, response);
            return;
        }
        String authorizationHeader = httpRequest.getHeader("Authorization");
        // 获取 token
        String token =  jwtService.getRealToken(authorizationHeader);
        R r = new R();
        httpResponse.setContentType("application/json;charset=UTF-8");
        if (token == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            r.setCode(ResultCode.UN_AUTHORIZED.getCode());
            r.setMsg(ResultCode.UN_AUTHORIZED.getMessage());
            httpResponse.getWriter().write(JSON.toJSONString(r));
            return;  // 拦截请求，返回 401 错误
        }else {
            String jti = jwtService.getJti(token);
            //检查是否在黑名单
            boolean inBlackList = Boolean.TRUE.equals(redisTemplate.hasKey(Const.BLACKLIST_JTI+ jti));
            if (inBlackList ||!jwtService.verifyToken(token)){
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                r.setCode(ResultCode.UN_AUTHORIZED.getCode());
                r.setMsg(ResultCode.UN_AUTHORIZED.getMessage());
                httpResponse.getWriter().write(JSON.toJSONString(r));
                return;  // 拦截请求，返回 401 错误
            }
        }

        // 继续执行请求链
        chain.doFilter(request, response);
    }


    // 检查请求路径是否是免验证路径
    private boolean isExcludedPath(String requestURI) {
        return requestURI.equals("/user/login")
                || requestURI.equals("/user/register")
                || requestURI.equals("/user/getAccessToken");
    }
}
