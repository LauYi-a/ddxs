package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.exception.ExceptionUtils;
import com.ddx.sys.entity.SysUserResource;
import com.ddx.sys.mapper.SysUserResourceMapper;
import com.ddx.sys.service.ISysUserResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SysUserResourceServiceImpl
 * @Description: 用户资源关联表 服务实现类
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
@Service
public class SysUserResourceServiceImpl extends ServiceImpl<SysUserResourceMapper, SysUserResource> implements ISysUserResourceService {

    @Override
    public Boolean addUserResourceId(List<Long> resourceIds, Long userId) {
        try {
            resourceIds.forEach(resourceId ->{
                Boolean is = baseMapper.insert(SysUserResource.builder()
                        .resourceId(resourceId)
                        .userId(userId)
                        .build())>0;
                ExceptionUtils.errorBusinessException(!is, CommonEnumConstant.PromptMessage.ADD_USER_RESOURCE_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean saveOrDeleteUserResourceId(Long userId, List<Long> resourceIds, Boolean isDelete, Boolean isSave) {
        try {
            //查询出当前用户所有的资源
            List<SysUserResource> sysUserResourceList = baseMapper.selectList(new QueryWrapper<SysUserResource>().lambda().eq(SysUserResource::getUserId,userId));
            //处理新增逻辑
            if (isSave) {
                if (sysUserResourceList.size() == 0){
                    Boolean isSaveOk = this.addUserResourceId(resourceIds,userId);
                    if (!isSaveOk){
                        return isSaveOk;
                    }
                }else{
                    resourceIds.forEach(resourceId -> {
                        Boolean isCount = sysUserResourceList.stream().filter(resourceObj -> resourceId.equals(resourceObj.getResourceId())).count() == 0;
                        Integer is = isCount ? baseMapper.insert(SysUserResource.builder()
                                .resourceId(resourceId)
                                .userId(userId)
                                .build()) : 0;
                    });
                }
            }
            //处理删除逻辑
            if (isDelete){
                List<Long> ids = new ArrayList<>();
                if (!CollectionUtils.isEmpty(resourceIds)){
                    sysUserResourceList.forEach(resourceObj -> {
                        Boolean isCount = resourceIds.stream().filter(resourceId -> resourceId.equals(resourceObj.getResourceId())).count() != 0;
                        if (isCount)
                            ids.add(resourceObj.getId());
                    });
                }else{
                    ids.addAll(sysUserResourceList.stream().map(SysUserResource::getId).collect(Collectors.toList()));
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
    public Boolean batchDeleteByUserIds(List<Long> userIds) {
        try {
            //根据用户id删除资源绑定关系
            userIds.forEach(userId ->{
                Boolean isDeleteOk = this.saveOrDeleteUserResourceId(userId,null,true,false);
                ExceptionUtils.errorBusinessException(!isDeleteOk, CommonEnumConstant.PromptMessage.DELETE_USER_RESOURCE_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
