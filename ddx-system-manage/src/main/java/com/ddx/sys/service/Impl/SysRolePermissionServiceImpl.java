package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.exception.ExceptionUtils;
import com.ddx.sys.entity.SysRolePermission;
import com.ddx.sys.mapper.SysRolePermissionMapper;
import com.ddx.sys.service.ISysRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SysRolePermissionServiceImpl
 * @Description: 角色权限表 服务实现类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Override
    public Boolean addRolePermissionId(List<Long> permissionIds,Long roleId) {
        try {
            permissionIds.forEach(permissionId ->{
               Boolean is = baseMapper.insert( SysRolePermission.builder()
                        .roleId(roleId)
                        .permissionId(permissionId)
                        .build())>0;
                ExceptionUtils.errorBusinessException(!is, CommonEnumConstant.PromptMessage.ADD_ROLE_PERMISSION_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean saveOrDeleteRolePermissionId(Long roleId, List<Long> permissionIds,Boolean isDelete,Boolean isSave) {
        try {
            //查询出当前角色所有的权限
            List<SysRolePermission>  rolePermissionsList = baseMapper.selectList(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId,roleId));
            //处理新增逻辑
            if (isSave) {
                if (rolePermissionsList.size() == 0){
                    Boolean isSaveOk = this.addRolePermissionId(permissionIds,roleId);
                    if (!isSaveOk){
                        return isSaveOk;
                    }
                }else{
                    permissionIds.forEach(permissionId -> {
                        Boolean isCount = rolePermissionsList.stream().filter(rolePermissionsIdObj -> permissionId.equals(rolePermissionsIdObj.getPermissionId())).count() == 0;
                        Integer is = isCount ? baseMapper.insert(SysRolePermission.builder()
                                .permissionId(permissionId)
                                .roleId(roleId)
                                .build()) : 0;
                    });
                }
            }
            //处理删除逻辑
            if (isDelete) {
                List<Long> ids = new ArrayList<>();
                if (!CollectionUtils.isEmpty(permissionIds)){
                    rolePermissionsList.forEach(rolePermissionsObj -> {
                        Boolean isCount = permissionIds.stream().filter(permissionId -> permissionId.equals(rolePermissionsObj.getPermissionId())).count() != 0;
                        if (isCount)
                            ids.add(rolePermissionsObj.getId());
                    });
                }else{
                    ids.addAll(rolePermissionsList.stream().map(SysRolePermission::getId).collect(Collectors.toList()));
                }
                baseMapper.deleteBatchIds(ids);
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean batchDeleteByRoleIds(List<Long> roleIds) {
        try {
            //查询出当前所有角色的所有的资源
            roleIds.forEach(roleId ->{
                Boolean isDeleteOk = this.saveOrDeleteRolePermissionId(roleId,null,true,false);
                ExceptionUtils.errorBusinessException(!isDeleteOk, CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
