package com.boruomi.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boruomi.business.model.entity.SysUserEntity;
import com.boruomi.business.model.entity.Token;
import com.boruomi.business.model.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 用户 服务类
 */
public interface ISysUserService extends IService<SysUserEntity> {
     Optional<SysUserVO> getByUsername(String account);

    /**
     * 注册用户
     * @param user
     */
    void registerUser(SysUserEntity user);

    /**
     * 登录
     * @param user
     * @return
     */
    Token login(SysUserEntity user);

    /**
     * 退出
     * @param token
     * @return
     */
    void loginOut(Token token);
}
