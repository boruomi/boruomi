package com.boruomi.business.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boruomi.business.mapper.SysPermissionMapper;
import com.boruomi.business.model.entity.SysPermissionEntity;
import com.boruomi.business.service.ISysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SysPermissionService extends ServiceImpl<SysPermissionMapper, SysPermissionEntity> implements ISysPermissionService {

}
