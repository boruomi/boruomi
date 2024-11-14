package com.boruomi.business.controller;

import com.boruomi.business.model.entity.SysUserEntity;
import com.boruomi.business.model.entity.Token;
import com.boruomi.business.service.Impl.SysUserService;
import com.boruomi.common.response.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class SysUserController {
    private final SysUserService sysUserService;

    @GetMapping("/getUserList")
    public R getUserList() {
        List<SysUserEntity> list = sysUserService.list();
        return R.data(list);
    }

    @PostMapping("/register")
    public R registerUser(@RequestBody SysUserEntity user) {
        try {
            sysUserService.registerUser(user);
            return R.success("register user success");
        } catch (Exception e) {
            log.error("register user fail",e);
            return R.fail("register user fail");
        }
    }

    @PostMapping("/login")
    public R login(@RequestBody SysUserEntity user) {
        try {
            Token token = sysUserService.login(user);
            return R.data(token,"login success");
        } catch (Exception e) {
            log.error("login fail",e);
            return R.fail("login fail");
        }
    }

    @PostMapping("/loginOut")
    public R loginOut(HttpServletRequest request) {
        try {
            String accessToken = request.getHeader("Authorization");
            String refreshToken = request.getHeader("refreshToken");
            Token token = Token.builder().accessToken(accessToken).refreshToken(refreshToken).build();
            sysUserService.loginOut(token);
            return R.success("loginOut success");
        } catch (Exception e) {
            log.error("loginOut fail",e);
            return R.fail("loginOut fail");
        }
    }

    @GetMapping("/getAccessToken")
    public R getAccessToken(HttpServletRequest request) {
        try {
            String refreshToken = request.getHeader("Authorization");
            String token = sysUserService.getAccessToken(refreshToken);
            return R.data(token);
        } catch (Exception e) {
            log.error("getAccessToken fail",e);
            return R.fail("getAccessToken fail");
        }
    }
}
