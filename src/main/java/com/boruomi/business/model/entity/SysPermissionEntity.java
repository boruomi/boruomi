package com.boruomi.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 权限 实体类
 */
@Data
@TableName("sys_permission")
public class SysPermissionEntity {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 权限名称
     */
    private String permission_name;
    /**
     * 描述
     */
    private String description;
    /**
     * 权限路径
     */
    private String path;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
