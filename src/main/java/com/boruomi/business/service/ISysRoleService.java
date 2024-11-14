package com.boruomi.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boruomi.business.model.entity.SysPermissionEntity;
import com.boruomi.business.model.entity.SysRoleEntity;

import java.util.List;

/**
 * 角色 服务类
 */
public interface ISysRoleService extends IService<SysRoleEntity> {
    /**
     * 根据角色id获取权限列表
     * @param roleIds
     * @return
     */
    List<SysPermissionEntity> getPermissionsByRoleId(List<Long> roleIds);
}
