package com.boruomi.business.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boruomi.business.mapper.SysRoleMapper;
import com.boruomi.business.model.entity.SysRoleEntity;
import com.boruomi.business.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements ISysRoleService {

}
