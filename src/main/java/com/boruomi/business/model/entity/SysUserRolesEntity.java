package com.boruomi.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户 实体类
 */
@Data
@TableName("sys_user_roles")
public class SysUserRolesEntity implements Serializable {
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 角色id
	 */
	private Long roleId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
}
