package com.boruomi.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boruomi.business.model.entity.SysRoleEntity;
import com.boruomi.business.model.entity.SysUserEntity;
import com.boruomi.business.model.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * role 接口
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {
}
