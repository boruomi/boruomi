package com.boruomi.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户 实体类
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 账号
	 */
	private String account;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 状态（0：未激活 1：已激活 2：已禁用  3：已锁定 4：已删除）
	 */
	private Integer status;
}
