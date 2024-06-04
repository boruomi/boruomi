package com.boruomi.business.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boruomi.business.mapper.SysUserMapper;
import com.boruomi.business.model.entity.SysUserEntity;
import com.boruomi.business.model.vo.SysUserVO;
import com.boruomi.business.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUserEntity> implements ISysUserService {
    @Autowired
    private  SysUserMapper sysUserMapper;

    @Override
    public Optional<SysUserVO> getByUsername(String account) {
        return sysUserMapper.findByUsername(account);
    }
}
