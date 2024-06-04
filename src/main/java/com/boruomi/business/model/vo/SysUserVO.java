package com.boruomi.business.model.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boruomi.business.model.entity.SysUserEntity;


import lombok.Data;

import java.util.List;

/**
 * 用户 实体类
 */
@Data
public class SysUserVO extends SysUserEntity {
    private String roles;
}
