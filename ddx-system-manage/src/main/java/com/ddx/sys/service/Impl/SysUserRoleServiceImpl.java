package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.sys.entity.SysUserRole;
import com.ddx.sys.mapper.SysUserRoleMapper;
import com.ddx.sys.service.ISysUserRoleService;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SysUserRoleServiceImpl
 * @Description: 用户和角色关联表 服务实现类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public Boolean addUserRoleId(List<Long> roles, Long userId) {
        try {
            roles.forEach(role ->{
                Boolean is = baseMapper.insert(SysUserRole.builder()
                        .roleId(role)
                        .userId(userId)
                        .build())>0;
                ExceptionUtils.businessException(!is, CommonEnumConstant.PromptMessage.ADD_USER_ROLE_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean saveOrDeleteUserRoleId(Long userId, List<Long> roleIds, Boolean isDelete, Boolean isSaveOrUpdate) {
        try {
            //查询出当前用户所有的角色
            List<SysUserRole> sysUserRoleList = baseMapper.selectList(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId,userId));
            //直接新增
            if (CollectionUtils.isEmpty(sysUserRoleList)){
                return this.addUserRoleId(roleIds,userId);
            }else{
                List<Long> oldIds = sysUserRoleList.stream().map(SysUserRole::getId).collect(Collectors.toList());
                //先删除在新增
                if (isSaveOrUpdate) {
                    baseMapper.deleteBatchIds(oldIds);
                    return this.addUserRoleId(roleIds,userId);
                }
                if (isDelete){
                    baseMapper.deleteBatchIds(!CollectionUtils.isEmpty(roleIds)?roleIds:oldIds);
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
            //根据用户ID删除角色绑定关系
            userIds.forEach(userId ->{
                Boolean isDeleteOk = this.saveOrDeleteUserRoleId(Long.valueOf(userId.toString()),null,true,false);
                ExceptionUtils.businessException(!isDeleteOk, CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
