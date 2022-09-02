package com.ddx.sys.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.sys.entity.SysUserRole;

import java.util.List;

/**
 * @ClassName: ISysUserRoleService
 * @Description: 用户和角色关联表 服务类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 绑定用户角色
     * @param roles
     * @param userId
     * @return
     */
    Boolean addUserRoleId(List<Long> roles,Long userId);

    /**
     * 新增或删除用户角色绑定关系
     * @param userId
     * @param roleIds
     * @param isDelete
     * @param isSaveOrUpdate
     * @return
     */
    Boolean saveOrDeleteUserRoleId(Long userId,List<Long> roleIds,Boolean isDelete,Boolean isSaveOrUpdate);

    /**
     * 根据用户id集合删除用户角色绑定关系
     * @param userIds
     * @return
     */
    Boolean batchDeleteByUserIds(List<Long> userIds);
}
