package com.ddx.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.dto.req.BatchDeleteKey;
import com.ddx.basis.dto.req.DeleteKey;
import com.ddx.basis.dto.resp.PaginatedResult;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.PageUtil;
import com.ddx.sys.dto.req.sysRole.SysRoleAddReq;
import com.ddx.sys.dto.req.sysRole.SysRoleEditReq;
import com.ddx.sys.dto.req.sysRole.SysRoleQueryReq;
import com.ddx.sys.dto.resp.sysRole.RoleKeyValResp;
import com.ddx.sys.entity.SysRole;
import com.ddx.sys.service.ISysPermissionService;
import com.ddx.sys.service.ISysRolePermissionService;
import com.ddx.sys.service.ISysRoleService;
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


/**
 * @ClassName: SysRoleController
 * @Description: 角色表 前端控制器
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-role")
@Api(tags = "角色控制层")
public class SysRoleController {

    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysPermissionService iSysPermissionService;
    @Autowired
    private ISysRolePermissionService iSysRolePermissionService;

    @PostMapping("/select-role-key-val-all")
    @ApiOperation(value = "查询所有角色键值", notes = "角色表")
    public ResponseData<List<RoleKeyValResp>> getRoleListAll() {
        log.info("select role key val all...");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,iSysRoleService.selectRoleKeyAndValAll());
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询角色列表", notes = "角色表")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysRoleQueryReq sysRoleQueryReq) {
        log.info("get SysRole list..");
        int page = PageUtil.parsePage(sysRoleQueryReq.getPage(), ConstantUtils.PAGE);
        int perPage = PageUtil.parsePerPage(sysRoleQueryReq.getPerPage(), ConstantUtils.PER_PAGE);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysRoleService.selectPage(new Page<>(page, perPage), sysRoleQueryReq));
    }

    @ApiOperation(value = "添加角色", notes = "角色表")
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(@Validated @RequestBody SysRoleAddReq  sysRoleAddReq) {
        log.info("add SysRole start..");
        return iSysRoleService.addRoleInfo(sysRoleAddReq);
    }

    @ApiOperation(value = "修改角色", notes = "角色表")
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysRoleEditReq sysRoleEditReq) {
        log.info("edit SysRole start...");
        SysRole sysRole = iSysRoleService.getOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getId,sysRoleEditReq.getId()).last("limit 1"));
        ExceptionUtils.errorBusinessException(sysRole == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysRoleEditReq, sysRole);
        Boolean yesOrNo = iSysRoleService.update(sysRole,new QueryWrapper<SysRole>().lambda().eq(SysRole::getId,sysRole.getId()));
        ExceptionUtils.errorBusinessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysRolePermissionService.saveOrDeleteRolePermissionId(sysRole.getId(),sysRoleEditReq.getRolePermissionId(),false,true), CommonEnumConstant.PromptMessage.ADD_ROLE_PERMISSION_ERROR);
        ExceptionUtils.errorBusinessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "删除角色", notes = "角色表")
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@Validated @RequestBody DeleteKey deleteKey) {
        log.info("delete SysRole start...");
        ExceptionUtils.errorBusinessException(!iSysRoleService.removeById(Long.valueOf(deleteKey.getKeyWord().toString())),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysRolePermissionService.saveOrDeleteRolePermissionId(Long.valueOf(deleteKey.getKeyWord().toString()),null,true,false), CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
        ExceptionUtils.errorBusinessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "批量删除角色", notes = "角色表")
    @PostMapping("/batch-delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysRole start...");
        ExceptionUtils.errorBusinessException(!iSysRoleService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysRolePermissionService.batchDeleteByRoleIds(batchDeleteKey.getKeyWords()), CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
        ExceptionUtils.errorBusinessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
