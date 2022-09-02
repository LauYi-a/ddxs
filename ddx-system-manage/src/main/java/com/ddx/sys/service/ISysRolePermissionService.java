package com.ddx.sys.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.sys.entity.SysRolePermission;

import java.util.List;

/**
 * @ClassName: ISysRolePermissionService
 * @Description: 角色权限表 服务类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 角色权限中间表绑定
     * @param permissionIds
     * @param roleId
     * @return
     */
    Boolean addRolePermissionId(List<Long> permissionIds,Long roleId);

    /**
     * 新增或删除权限绑定关系
     * @param roleId
     * @param permissionIds
     * @param isDelete
     * @param isSaveOrUpdate
     * @return
     */
    Boolean saveOrDeleteRolePermissionId(Long roleId,List<Long> permissionIds,Boolean isDelete,Boolean isSaveOrUpdate);

    /**
     * 根据角色id集合删除角色权限绑定关系
     * @param roleIds
     * @return
     */
    Boolean batchDeleteByRoleIds(List<Long> roleIds);

}
