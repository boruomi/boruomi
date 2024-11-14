package com.boruomi.business.controller;

import com.boruomi.business.model.entity.SysRolePermissionsEntity;
import com.boruomi.business.service.Impl.SysRolePermissionsService;
import com.boruomi.common.response.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/rolePermissions")
public class SysRolePermissionsController {
    private final SysRolePermissionsService sysRolePermissionsService;

    @GetMapping("/List")
    public R getList() {
        List<SysRolePermissionsEntity> list = sysRolePermissionsService.list();
        return R.data(list);
    }
}
