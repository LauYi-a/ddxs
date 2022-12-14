package com.ddx.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.sys.entity.SysUser;
import com.ddx.sys.entity.SysUserResource;
import com.ddx.sys.model.req.sysUser.*;
import com.ddx.sys.model.resp.sysResource.TreeMenuAndElAuthResp;
import com.ddx.sys.model.resp.sysUser.SysUserResp;
import com.ddx.sys.model.vo.resource.MenuElVo;
import com.ddx.sys.model.vo.resource.UserTreeMenuVo;
import com.ddx.sys.service.ISysResourceService;
import com.ddx.sys.service.ISysUserResourceService;
import com.ddx.sys.service.ISysUserRoleService;
import com.ddx.sys.service.ISysUserService;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.req.BatchDeleteKey;
import com.ddx.util.basis.model.req.DeleteKey;
import com.ddx.util.basis.model.resp.PaginatedResult;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.PageUtil;
import com.ddx.util.basis.utils.sm3.SM3Digest;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: SysUserController
 * @Description: 用户信息表 前端控制器
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-user")
@Api(tags = "平台用户信息控制层")
public class SysUserController {

    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysUserResourceService iSysUserResourceService;
    @Autowired
    private ISysResourceService iSysResourceService;


    @PostMapping("/get-user-tree-menu")
    @ApiOperation(httpMethod = "POST",value = "查询用户树菜单")
    public ResponseData<TreeMenuAndElAuthResp> getUserTreeMenu(@Validated @RequestBody QueryUserMenuTreeReq queryUserMenuTreeReq){
        log.info("getUserTreeMenu SysResource start..");
        List<Long> userMenuIds =  iSysUserResourceService.list(new QueryWrapper<SysUserResource>().lambda()
                .eq(SysUserResource::getUserId, queryUserMenuTreeReq.getUserId())).stream().map(SysUserResource::getResourceId).collect(Collectors.toList());
        ExceptionUtils.businessException(userMenuIds.size() == 0,CommonEnumConstant.PromptMessage.USER_MENU_ISNULL_ERROR);
        List<UserTreeMenuVo> treeMenuRespList = iSysResourceService.getMenuTreeByUserAndServiceResourceIds(userMenuIds,queryUserMenuTreeReq.getServiceModule());
        ExceptionUtils.businessException(treeMenuRespList.size() == 0,CommonEnumConstant.PromptMessage.USER_MENU_ISNULL_ERROR);
        List<MenuElVo> menuElRespList = iSysResourceService.getMenuElByAndServiceResourceIds(userMenuIds,queryUserMenuTreeReq.getServiceModule(),true);
        List<String> services = iSysResourceService.getServiceList(userMenuIds);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, TreeMenuAndElAuthResp.builder().treeMenu(treeMenuRespList).menuEl(menuElRespList).serviceList(services).build());
    }


    @PostMapping("/update-by-userId")
    @ApiOperation(httpMethod = "POST",value = "根据用户身份ID修改基本信息")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateByUserId(@Validated @RequestBody UpdateUserBasisInfo updateUserBasisInfo){
        log.info("update-by-userId start...");
        SysUser sysUser = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUserId,updateUserBasisInfo.getUserId()).last("limit 1"));
        ExceptionUtils.businessException(sysUser == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(updateUserBasisInfo, sysUser);
        Boolean yesOrNo = iSysUserService.update(sysUser,new QueryWrapper<SysUser>().lambda().eq(SysUser::getId,sysUser.getId()));
        ExceptionUtils.businessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/password-change")
    @ApiOperation(httpMethod = "POST",value = "修改用户密码")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse passwordChange(@Validated @RequestBody PasswordChangeReq passwordChangeReq){
        log.info("password-change start...");
        ExceptionUtils.businessException(!passwordChangeReq.getNewPassword().equals(passwordChangeReq.getNewPasswordYes()), CommonEnumConstant.PromptMessage.USER_NEW_PASSWORD_ERROR);
        SysUser sysUser = iSysUserService.getById(passwordChangeReq.getId());
        ExceptionUtils.businessException(sysUser == null, CommonEnumConstant.PromptMessage.QUOTA_USER_NOT_ERROR);
        ExceptionUtils.businessException(!Objects.equals(sysUser.getPassword(),SM3Digest.decode(passwordChangeReq.getOldPassword())), CommonEnumConstant.PromptMessage.USER_OLD_PASSWORD_ERROR);
        sysUser.setPassword(SM3Digest.decode(passwordChangeReq.getNewPassword()));
        ExceptionUtils.businessException(!iSysUserService.updateById(sysUser),CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/get-user-info-byId")
    @ApiOperation(httpMethod = "POST",value = "根据用户id查询用户详细信息")
    public ResponseData<SysUserResp> getUserInfoById(@Validated @RequestBody QueryUserInfoReq queryUserInfoReq) {
        log.info("get user info by id start...");
        SysUser sysUser = iSysUserService.getById(queryUserInfoReq.getId());
        ExceptionUtils.businessException(sysUser == null, CommonEnumConstant.PromptMessage.QUOTA_USER_NOT_ERROR);
        SysUserResp sysUserResp = iSysUserService.selectUserInfoByUsers(Lists.newArrayList(sysUser)).stream().findFirst().orElse(null);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, sysUserResp);
    }

    @PostMapping("/list")
    @ApiOperation(httpMethod = "POST",value = "查询用户信息列表")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysUserQueryReq sysUserQuotaReq) {
        log.info("get SysUser list...");
        int page = PageUtil.parsePage(sysUserQuotaReq.getPage(), BasisConstant.PAGE);
        int perPage = PageUtil.parsePerPage(sysUserQuotaReq.getPerPage(), BasisConstant.PER_PAGE);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysUserService.selectPage(new Page<>(page, perPage),sysUserQuotaReq));
    }

    @PostMapping("/add")
    @ApiOperation(httpMethod = "POST",value = "添加用户信息")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(@Validated @RequestBody SysUserAddReq  sysUserAddReq) {
        log.info("add SysUser start...");
        return iSysUserService.addUserInfo(sysUserAddReq);
    }

    @PostMapping("/edit")
    @ApiOperation(httpMethod = "POST",value = "修改用户信息")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysUserEditReq sysUserEditReq) {
        log.info("edit SysUser start...");
        SysUser sysUser = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getId,sysUserEditReq.getId()).last("limit 1"));
        ExceptionUtils.businessException(sysUser == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysUserEditReq, sysUser);
        ExceptionUtils.businessException(!iSysUserService.update(sysUser,new QueryWrapper<SysUser>().lambda().eq(SysUser::getId,sysUser.getId())),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysUserRoleService.saveOrDeleteUserRoleId(sysUser.getId(),sysUserEditReq.getRoleIds(),false,true), CommonEnumConstant.PromptMessage.ADD_USER_ROLE_ERROR);
        ExceptionUtils.businessException(!iSysUserResourceService.saveOrDeleteUserResourceId(sysUser.getId(),sysUserEditReq.getResourceIds(),false,true), CommonEnumConstant.PromptMessage.ADD_USER_RESOURCE_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/delete")
    @ApiOperation(httpMethod = "POST",value = "删除用户信息")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@Validated @RequestBody DeleteKey deleteKey) {
        log.info("delete SysUser start...");
        ExceptionUtils.businessException(!iSysUserService.removeById(Long.valueOf(deleteKey.getKeyWord().toString())),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysUserRoleService.saveOrDeleteUserRoleId(Long.valueOf(deleteKey.getKeyWord().toString()),null,true,false), CommonEnumConstant.PromptMessage.DELETE_USER_ROLE_ERROR);
        ExceptionUtils.businessException(!iSysUserResourceService.saveOrDeleteUserResourceId(Long.valueOf(deleteKey.getKeyWord().toString()),null,true,false), CommonEnumConstant.PromptMessage.DELETE_USER_RESOURCE_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/batch-delete")
    @ApiOperation(httpMethod = "POST",value = "批量删除用户信息")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysUser start...");
        ExceptionUtils.businessException(!iSysUserService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysUserRoleService.batchDeleteByUserIds(batchDeleteKey.getKeyWords()), CommonEnumConstant.PromptMessage.DELETE_USER_ROLE_ERROR);
        ExceptionUtils.businessException(!iSysUserResourceService.batchDeleteByUserIds(batchDeleteKey.getKeyWords()), CommonEnumConstant.PromptMessage.DELETE_USER_RESOURCE_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
