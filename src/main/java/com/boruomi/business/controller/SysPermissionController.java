package com.boruomi.business.controller;

import com.boruomi.business.model.entity.SysPermissionEntity;
import com.boruomi.business.service.Impl.SysPermissionService;
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
@RequestMapping("/permission")
public class SysPermissionController {
    private final SysPermissionService sysPermissionService;

    @GetMapping("/List")
    public R getList() {
        List<SysPermissionEntity> list = sysPermissionService.list();
        return R.data(list);
    }
}
