package com.ddx.auth.service.Impl;

import com.ddx.auth.entity.SysUserRole;
import com.ddx.auth.mapper.SysUserRoleMapper;
import com.ddx.auth.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SysUserRoleServiceImpl
 * @Description: 用户和角色关联表 服务实现类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

}
