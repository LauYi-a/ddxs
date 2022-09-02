package com.ddx.sys.service;

import com.ddx.sys.dto.resp.sysResource.ResourceIdsResp;
import com.ddx.sys.entity.SysUserResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @ClassName: ISysUserResourceService
 * @Description: 用户资源关联表 服务类
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
public interface ISysUserResourceService extends IService<SysUserResource> {

    /**
     * 绑定用户资源
     * @param resourceIds
     * @param userId
     * @return
     */
    Boolean addUserResourceId(List<Long> resourceIds, Long userId);

    /**
     * 新增或删除用户资源绑定关系
     * @param userId
     * @param resourceIds
     * @param isDelete
     * @param isSaveOrUpdate
     * @return
     */
    Boolean saveOrDeleteUserResourceId(Long userId,List<Long> resourceIds,Boolean isDelete,Boolean isSaveOrUpdate);

    /**
     * 根据用户id集合删除用户资源绑定关系
     * @param userIds
     * @return
     */
    Boolean batchDeleteByUserIds(List<Long> userIds);

    /**
     * 根据用户id查询用户资源绑定关系Id集合
     * @param userId
     * @return
     */
    ResourceIdsResp selectUserResourceIdsByUserId(Long userId);
}
