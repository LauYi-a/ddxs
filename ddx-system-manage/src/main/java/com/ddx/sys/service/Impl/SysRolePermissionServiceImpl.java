package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.sys.entity.SysRolePermission;
import com.ddx.sys.mapper.SysRolePermissionMapper;
import com.ddx.sys.service.ISysRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
                ExceptionUtils.businessException(!is, CommonEnumConstant.PromptMessage.ADD_ROLE_PERMISSION_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean saveOrDeleteRolePermissionId(Long roleId, List<Long> permissionIds,Boolean isDelete,Boolean isSaveOrUpdate) {
        try {
            //查询出当前角色所有的权限
            List<SysRolePermission>  rolePermissionsList = baseMapper.selectList(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId,roleId));
            //直接新增
            if (CollectionUtils.isEmpty(rolePermissionsList)){
                return this.addRolePermissionId(permissionIds,roleId);
            }else{
                List<Long> oldIds = rolePermissionsList.stream().map(SysRolePermission::getId).collect(Collectors.toList());
                //先删除在新增
                if (isSaveOrUpdate) {
                    baseMapper.deleteBatchIds(oldIds);
                    return this.addRolePermissionId(permissionIds,roleId);
                }
                if (isDelete){
                    baseMapper.deleteBatchIds(!CollectionUtils.isEmpty(permissionIds)?permissionIds:oldIds);
                }
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean batchDeleteByRoleIds(List<?> roleIds) {
        try {
            //查询出当前所有角色的所有的资源
            roleIds.forEach(roleId ->{
                Boolean isDeleteOk = this.saveOrDeleteRolePermissionId(Long.valueOf(roleId.toString()),null,true,false);
                ExceptionUtils.businessException(!isDeleteOk, CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
