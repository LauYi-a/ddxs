package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.sys.entity.SysUserResource;
import com.ddx.sys.mapper.SysUserResourceMapper;
import com.ddx.sys.service.ISysUserResourceService;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
                ExceptionUtils.businessException(!is, CommonEnumConstant.PromptMessage.ADD_USER_RESOURCE_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean saveOrDeleteUserResourceId(Long userId, List<Long> resourceIds, Boolean isDelete, Boolean isSaveOrUpdate) {
        try {
            //查询出当前用户所有的资源
            List<SysUserResource> sysUserResourceList = baseMapper.selectList(new QueryWrapper<SysUserResource>().lambda().eq(SysUserResource::getUserId,userId));
            //直接新增
            if (CollectionUtils.isEmpty(sysUserResourceList)){
                return this.addUserResourceId(resourceIds,userId);
            }else{
                List<Long> oldIds = sysUserResourceList.stream().map(SysUserResource::getId).collect(Collectors.toList());
                //先删除在新增
                if (isSaveOrUpdate) {
                    baseMapper.deleteBatchIds(oldIds);
                    return this.addUserResourceId(resourceIds,userId);
                }
                if (isDelete){
                    baseMapper.deleteBatchIds(!CollectionUtils.isEmpty(resourceIds)?resourceIds:oldIds);
                }
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean batchDeleteByUserIds(List<?> userIds) {
        try {
            //根据用户id删除资源绑定关系
            userIds.forEach(userId ->{
                Boolean isDeleteOk = this.saveOrDeleteUserResourceId(Long.valueOf(userId.toString()),null,true,false);
                ExceptionUtils.businessException(!isDeleteOk, CommonEnumConstant.PromptMessage.DELETE_USER_RESOURCE_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Long> selectUserResourceIdsByUserId(Long userId) {
        List<Long> userMenuIds =  baseMapper.selectList(new QueryWrapper<SysUserResource>().lambda()
                .eq(SysUserResource::getUserId, userId)).stream().map(SysUserResource::getResourceId).collect(Collectors.toList());
        return userMenuIds;
    }
}
