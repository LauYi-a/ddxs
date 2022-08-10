package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.exception.ExceptionUtils;
import com.ddx.sys.entity.SysUserRole;
import com.ddx.sys.mapper.SysUserRoleMapper;
import com.ddx.sys.service.ISysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
                ExceptionUtils.errorBusinessException(!is, CommonEnumConstant.PromptMessage.ADD_USER_ROLE_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean saveOrDeleteUserRoleId(Long userId, List<Long> roleIds, Boolean isDelete, Boolean isSave) {
        try {
            //查询出当前用户所有的角色
            List<SysUserRole> sysUserRoleList = baseMapper.selectList(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId,userId));
            //处理新增逻辑
            if (isSave) {
                if (sysUserRoleList.size() == 0){
                    Boolean isSaveOk = this.addUserRoleId(roleIds,userId);
                    if (!isSaveOk){
                        return isSaveOk;
                    }
                }else{
                    roleIds.forEach(roleId -> {
                        Boolean isCount = sysUserRoleList.stream().filter(roleObj -> roleId.equals(roleObj.getRoleId())).count() == 0;
                        Integer is = isCount ? baseMapper.insert(SysUserRole.builder()
                                .roleId(roleId)
                                .userId(userId)
                                .build()) : 0;
                    });
                }
            }
            //处理删除逻辑
            if (isDelete){
                List<Long> ids = new ArrayList<>();
                if (!CollectionUtils.isEmpty(roleIds)){
                    sysUserRoleList.forEach(userRoleObj -> {
                        Boolean isCount = roleIds.stream().filter(roleId -> roleId.equals(userRoleObj.getRoleId())).count() != 0;
                        if (isCount)
                            ids.add(userRoleObj.getId());
                    });
                }else{
                    ids.addAll(sysUserRoleList.stream().map(SysUserRole::getId).collect(Collectors.toList()));
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
            //根据用户ID删除角色绑定关系
            userIds.forEach(userId ->{
                Boolean isDeleteOk = this.saveOrDeleteUserRoleId(userId,null,true,false);
                ExceptionUtils.errorBusinessException(!isDeleteOk, CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
            });
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
