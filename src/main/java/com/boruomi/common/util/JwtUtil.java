package com.boruomi.common.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    // 密钥
    private static final String SECRET_KEY = "4DcW0jjVVe/4eZZS/JpiCQ==";

    // 访问 Token 默认过期时间：15分钟
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 15 * 60 * 1000L;
    // 刷新 Token 默认过期时间：30天
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000L;

    // 生成访问 Token
    public static String createAccessToken(String userId, String username) {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");  // 签名算法
        header.put("typ", "JWT");    // Token 类型

        // 载荷信息：可以包含一些必要的用户信息
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("iat", new Date().getTime());  // 生成时间
        payload.put("exp", System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME);  // 过期时间

        // 生成并返回访问 Token
        return JWTUtil.createToken(payload, header, SECRET_KEY.getBytes());
    }

    // 生成刷新 Token
    public static String createRefreshToken(String userId, String username) {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");  // 签名算法
        header.put("typ", "JWT");    // Token 类型

        // 载荷信息：可以包含一些必要的用户信息
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("iat", new Date().getTime());  // 生成时间
        payload.put("exp", System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME);  // 过期时间

        // 生成并返回刷新 Token
        return JWTUtil.createToken(payload, header, SECRET_KEY.getBytes());
    }

    // 验证 Token（用于验证访问 Token 和刷新 Token）
    public static boolean verifyToken(String token) {
        try {
            // 使用密钥验证 Token
            return JWTUtil.verify(token, SECRET_KEY.getBytes());
        } catch (JWTException e) {
            System.out.println("Token 验证失败: " + e.getMessage());
            return false;
        }
    }

    // 解析 Token 获取载荷中的信息
    public static Map<String, Object> parseToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            if (jwt == null) {
                return null;
            }
            // 获取载荷中的信息
            return jwt.getPayloads();
        } catch (JWTException e) {
            System.out.println("Token 解析失败: " + e.getMessage());
            return null;
        }
    }

    // 获取 Token 中的用户信息（例如 userId）
    public static String getUserId(String token) {
        Map<String, Object> payload = parseToken(token);
        if (payload != null && payload.containsKey("userId")) {
            return (String) payload.get("userId");
        }
        return null;
    }

    public static String getUsername(String token) {
        Map<String, Object> payload = parseToken(token);
        if (payload != null && payload.containsKey("username")) {
            return (String) payload.get("username");
        }
        return null;
    }

    // 判断 Token 是否过期
    public static boolean isTokenExpired(String token) {
        Map<String, Object> payload = parseToken(token);
        if (payload != null && payload.containsKey("exp")) {
            long expTime = (Long) payload.get("exp");
            return System.currentTimeMillis() > expTime;
        }
        return false;
    }

    // 刷新 Token（如果过期，则通过刷新 Token 生成新的访问 Token）
    public static String refreshAccessToken(String refreshToken) {
        if (isTokenExpired(refreshToken)) {
            return null;  // 如果刷新 Token 已过期，则返回 null
        }

        Map<String, Object> payload = parseToken(refreshToken);
        if (payload != null) {
            String userId = (String) payload.get("userId");
            String username = (String) payload.get("username");
            return createAccessToken(userId, username);  // 刷新并生成新的访问 Token
        }
        return null;
    }

    // 主函数测试
    public static void main(String[] args) {
        // 生成访问 Token
        String accessToken = createAccessToken("12345", "john_doe");
        System.out.println("Generated Access Token: " + accessToken);

        // 生成刷新 Token
        String refreshToken = createRefreshToken("12345", "john_doe");
        System.out.println("Generated Refresh Token: " + refreshToken);

        // 验证访问 Token
        boolean isAccessTokenValid = verifyToken(accessToken);
        System.out.println("Is the access token valid? " + isAccessTokenValid);

        // 获取用户信息
        String userId = getUserId(accessToken);
        String username = getUsername(accessToken);
        System.out.println("User ID: " + userId);
        System.out.println("Username: " + username);

        // 检查访问 Token 是否过期
        boolean isAccessTokenExpired = isTokenExpired(accessToken);
        System.out.println("Is the access token expired? " + isAccessTokenExpired);

        // 使用刷新 Token 刷新访问 Token
        String newAccessToken = refreshAccessToken(refreshToken);
        if (newAccessToken != null) {
            System.out.println("Refreshed Access Token: " + newAccessToken);
        } else {
            System.out.println("Refresh token has expired.");
        }
    }
}
