package com.boruomi.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boruomi.business.mapper.SysRoleMapper;
import com.boruomi.business.mapper.SysUserMapper;
import com.boruomi.business.mapper.SysUserRolesMapper;
import com.boruomi.business.model.entity.SysPermissionEntity;
import com.boruomi.business.model.entity.SysUserEntity;
import com.boruomi.business.model.entity.SysUserRolesEntity;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUserEntity> implements ISysUserService {
    @Autowired
    private  SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;
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
        SysUserEntity entity = authenticate(user);
        if (null != entity){
            List<String> permissions = getPermissionsByUserId(entity.getId());
            String accessToken = jwtService.createAccessToken(entity.getId(), entity.getUserName(),permissions);
            String refreshToken = jwtService.createRefreshToken(entity.getId(), entity.getUserName());
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

    @Override
    public String getAccessToken(String refreshToken) {
        if (refreshToken == null) throw new RuntimeException("refreshToken is null");
        String realToken = jwtService.getRealToken(refreshToken);
        Long userId =Long.valueOf(jwtService.getUserId(realToken)) ;
        String username = jwtService.getUsername(realToken);
        List<String> permissions = getPermissionsByUserId(userId);
        return jwtService.createAccessToken(userId,username, permissions);
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
                    return sysUserEntity;
                }
            } catch (Exception e) {
                log.error("decrypt password fail",e);
            }
        }
        return null;
    }

    private List<String> getPermissionsByUserId(Long userId){
        LambdaQueryWrapper<SysUserRolesEntity> userRolesWrapper = new LambdaQueryWrapper<>();
        userRolesWrapper.eq(SysUserRolesEntity::getUserId,userId);
        List<SysUserRolesEntity> sysUserRolesEntities = sysUserRolesMapper.selectList(userRolesWrapper);
        if (!sysUserRolesEntities.isEmpty()){
            List<Long> roleIds = sysUserRolesEntities.stream()
                    .map(SysUserRolesEntity::getRoleId).toList();
          return  sysRoleMapper.getPermissionsByRoleId(roleIds).stream().map(SysPermissionEntity::getPath).toList();
        }
        return new ArrayList<>();
    }
}
