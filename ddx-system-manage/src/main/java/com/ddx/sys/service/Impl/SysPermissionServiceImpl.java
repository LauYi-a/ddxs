package com.ddx.sys.service.Impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.utils.RedisTemplateUtils;
import com.ddx.sys.dto.vo.permission.RolePermissionVo;
import com.ddx.sys.entity.SysPermission;
import com.ddx.sys.entity.SysRole;
import com.ddx.sys.entity.SysRolePermission;
import com.ddx.sys.mapper.SysPermissionMapper;
import com.ddx.sys.service.ISysPermissionService;
import com.ddx.sys.service.ISysRolePermissionService;
import com.ddx.sys.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private RedisTemplateUtils redisTemplate;

    public IPage<SysPermission> selectPage(IPage<SysPermission> arg0,  Wrapper<SysPermission> arg1){
        return this.baseMapper.selectPage(arg0, arg1);
    }

    @Override
    public Boolean initRolePermission() {
        try {
            List<RolePermissionVo> list=new ArrayList<>();
            baseMapper.selectList(new QueryWrapper<SysPermission>()).forEach(permission -> {
                List<SysRole> roles = sysRolePermissionService.list(new QueryWrapper<SysRolePermission>().lambda()
                        .eq(SysRolePermission::getPermissionId,permission.getId()))
                        .stream().map(k -> sysRoleService.getById(k.getRoleId())).collect(Collectors.toList());
                list.add(RolePermissionVo.builder()
                        .permissionId(permission.getId())
                        .url(permission.getUrl())
                        .permissionName(permission.getName())
                        .roles(roles)
                        .build());
            });
            //先删除Redis中原来的权限hash表
            redisTemplate.del(ConstantUtils.OAUTH_URLS);
            list.parallelStream().peek(k->{
                List<Object> roles=new ArrayList<>();
                if (CollectionUtil.isNotEmpty(k.getRoles())){
                    for (SysRole role : k.getRoles()) {
                        roles.add(ConstantUtils.ROLE_PREFIX+role.getCode());
                    }
                }
                //然后更新Redis中的权限
                redisTemplate.hset(ConstantUtils.OAUTH_URLS,k.getUrl(), roles);
            }).collect(Collectors.toList());
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
