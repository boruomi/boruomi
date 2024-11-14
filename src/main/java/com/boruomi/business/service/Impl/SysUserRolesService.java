package com.boruomi.business.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boruomi.business.mapper.SysUserRolesMapper;
import com.boruomi.business.model.entity.SysUserRolesEntity;
import com.boruomi.business.service.ISysUserRolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysUserRolesService extends ServiceImpl<SysUserRolesMapper, SysUserRolesEntity> implements ISysUserRolesService {

}
