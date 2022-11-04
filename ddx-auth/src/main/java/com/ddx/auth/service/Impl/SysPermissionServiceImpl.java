package com.ddx.auth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.auth.entity.SysPermission;
import com.ddx.auth.entity.SysRole;
import com.ddx.auth.entity.SysRolePermission;
import com.ddx.auth.mapper.SysPermissionMapper;
import com.ddx.auth.model.resp.SysRolePermissionResp;
import com.ddx.auth.service.ISysPermissionService;
import com.ddx.auth.service.ISysRolePermissionService;
import com.ddx.auth.service.ISysRoleService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: SysPermissionServiceImpl
 * @Description: 权限表 服务实现类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;
    @Autowired
    private ISysRoleService sysRoleService;

    public IPage<SysPermission> selectPage(IPage<SysPermission> arg0,  Wrapper<SysPermission> arg1){
        return this.baseMapper.selectPage(arg0, arg1);
    }

    @Override
    public List<SysRolePermissionResp> listRolePermission() {
        List<SysRolePermissionResp> list=new ArrayList<>();
        List<SysRole> sysRoles = sysRoleService.list(new QueryWrapper<SysRole>());
        List<SysRolePermission> rolePermissions = sysRolePermissionService.list(new QueryWrapper<SysRolePermission>());
        baseMapper.selectList(new QueryWrapper<SysPermission>()).forEach(permission -> {
            List<SysRole> roleList = Lists.newArrayList();
            rolePermissions.stream().filter(rolePermission -> Objects.equals(permission.getId(),rolePermission.getPermissionId()))
                    .collect(Collectors.toList()).forEach(rolePermission ->{
                List<SysRole> roles = sysRoles.stream().filter(role -> Objects.equals(rolePermission.getRoleId(), role.getId())).collect(Collectors.toList());
                roleList.addAll(roles);
            });
            list.add(SysRolePermissionResp.builder()
                    .permissionId(permission.getId())
                    .url(permission.getUrl())
                    .permissionName(permission.getName())
                    .roles(roleList)
                    .build());
        });
        return list;
    }
}
