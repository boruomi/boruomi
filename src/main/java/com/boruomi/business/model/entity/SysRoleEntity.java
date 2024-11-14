package com.boruomi.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 角色 实体类
 */
@Data
@TableName("sys_rule")
public class SysRoleEntity {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 角色
     */
    private String roleName;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
