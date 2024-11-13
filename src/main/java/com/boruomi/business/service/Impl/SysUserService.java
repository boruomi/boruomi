package com.boruomi.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boruomi.business.mapper.SysUserMapper;
import com.boruomi.business.model.entity.SysUserEntity;
import com.boruomi.business.model.entity.Token;
import com.boruomi.business.model.vo.SysUserVO;
import com.boruomi.business.service.ISysUserService;
import com.boruomi.common.Const;
import com.boruomi.common.util.AESUtil;
import com.boruomi.common.util.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUserEntity> implements ISysUserService {
    @Autowired
    private  SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private JwtService jwtService;
    @Override
    public Optional<SysUserVO> getByUsername(String account) {
        return sysUserMapper.findByUsername(account);
    }

    @Override
    public void registerUser(SysUserEntity user) {
        String password = user.getPassword();
        try {
            String encrypt = AESUtil.encrypt(password, AESUtil.getSecretKey());
            user.setPassword(encrypt);
            this.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Token login(SysUserEntity user) {
        SysUserEntity authenticate = authenticate(user);
        if (null != authenticate){
            String accessToken = jwtService.createAccessToken(user.getAccount(), user.getUserName());
            String refreshToken = jwtService.createRefreshToken(user.getAccount(), user.getUserName());
            return Token.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return null;
    }

    @Override
    public void loginOut(Token token) {
        String accessToken = jwtService.getRealToken(token.getAccessToken());
        String refreshToken = jwtService.getRealToken(token.getRefreshToken());
        jwtService.addBlackList(accessToken);
        jwtService.addBlackList(refreshToken);

    }

    /**
     * 验证用户
     * @param user
     * @return
     */
    private SysUserEntity authenticate(SysUserEntity user){
        LambdaQueryWrapper<SysUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserEntity::getAccount,user.getAccount());
        SysUserEntity sysUserEntity = baseMapper.selectOne(wrapper);
        if (null != sysUserEntity){
            String password = user.getPassword();
            try {
                String decrypt = AESUtil.decrypt(sysUserEntity.getPassword(), AESUtil.getSecretKey());
                if (decrypt.equals(password)){
                    return user;
                }
            } catch (Exception e) {
                log.error("decrypt password fail",e);
            }
        }
        return null;
    }
}
