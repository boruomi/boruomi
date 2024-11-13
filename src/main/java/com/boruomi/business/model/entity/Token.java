package com.boruomi.business.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    /**
     * 唯一标识
     */
    private String jti;
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
}
