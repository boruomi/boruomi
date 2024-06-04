package com.boruomi.business.controller;

import com.boruomi.business.model.entity.SysUserEntity;
import com.boruomi.business.service.Impl.SysUserService;
import com.boruomi.common.response.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class SysUserController {
    private final SysUserService sysUserService;

    @GetMapping("/getUserList")
    public R getUserList() {
        List<SysUserEntity> list = sysUserService.list();
        return R.data(list);
    }
}
