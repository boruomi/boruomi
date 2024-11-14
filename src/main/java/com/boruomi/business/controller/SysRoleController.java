package com.boruomi.business.controller;

import com.boruomi.business.model.entity.SysRoleEntity;
import com.boruomi.business.service.Impl.SysRoleService;
import com.boruomi.common.response.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/role")
public class SysRoleController {
    private final SysRoleService sysRoleService;

    @GetMapping("/List")
    public R getUserList() {
        List<SysRoleEntity> list = sysRoleService.list();
        return R.data(list);
    }
}
