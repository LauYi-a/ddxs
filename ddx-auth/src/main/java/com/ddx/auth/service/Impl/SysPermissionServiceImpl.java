package com.ddx.auth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.dto.resp.SysRolePermissionResp;
import com.ddx.auth.entity.SysPermission;
import com.ddx.auth.entity.SysRole;
import com.ddx.auth.entity.SysRolePermission;
import com.ddx.auth.mapper.SysPermissionMapper;
import com.ddx.auth.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.auth.service.ISysRolePermissionService;
import com.ddx.auth.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.List;
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
        baseMapper.selectList(new QueryWrapper<SysPermission>()).forEach(permission -> {
            List<SysRole> roles = sysRolePermissionService.list(new QueryWrapper<SysRolePermission>().lambda()
                    .eq(SysRolePermission::getPermissionId,permission.getId()))
                    .stream().map(k -> sysRoleService.getById(k.getRoleId())).collect(Collectors.toList());
            list.add(SysRolePermissionResp.builder()
                    .permissionId(permission.getId())
                    .url(permission.getUrl())
                    .permissionName(permission.getName())
                    .roles(roles)
                    .build());
        });
        return list;
    }
}
