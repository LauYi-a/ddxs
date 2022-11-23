package com.ddx.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.sys.entity.SysRole;
import com.ddx.sys.model.req.sysRole.SysRoleAddReq;
import com.ddx.sys.model.req.sysRole.SysRoleEditReq;
import com.ddx.sys.model.req.sysRole.SysRoleQueryReq;
import com.ddx.sys.model.resp.sysRole.RoleKeyValResp;
import com.ddx.sys.service.ISysPermissionService;
import com.ddx.sys.service.ISysRolePermissionService;
import com.ddx.sys.service.ISysRoleService;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.req.BatchDeleteKey;
import com.ddx.util.basis.model.req.DeleteKey;
import com.ddx.util.basis.model.resp.PaginatedResult;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.PageUtil;
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
    @ApiOperation(httpMethod = "POST",value = "查询所有角色键值")
    public ResponseData<List<RoleKeyValResp>> getRoleListAll() {
        log.info("select role key val all...");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,iSysRoleService.selectRoleKeyAndValAll());
    }

    @PostMapping("/list")
    @ApiOperation(httpMethod = "POST",value = "查询角色列表")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysRoleQueryReq sysRoleQueryReq) {
        log.info("get SysRole list..");
        int page = PageUtil.parsePage(sysRoleQueryReq.getPage(), BasisConstant.PAGE);
        int perPage = PageUtil.parsePerPage(sysRoleQueryReq.getPerPage(), BasisConstant.PER_PAGE);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysRoleService.selectPage(new Page<>(page, perPage), sysRoleQueryReq));
    }

    @PostMapping("/add")
    @ApiOperation(httpMethod = "POST",value = "添加角色")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(@Validated @RequestBody SysRoleAddReq  sysRoleAddReq) {
        log.info("add SysRole start..");
        return iSysRoleService.addRoleInfo(sysRoleAddReq);
    }

    @PostMapping("/edit")
    @ApiOperation(httpMethod = "POST",value = "修改角色")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysRoleEditReq sysRoleEditReq) {
        log.info("edit SysRole start...");
        SysRole sysRole = iSysRoleService.getOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getId,sysRoleEditReq.getId()).last("limit 1"));
        ExceptionUtils.businessException(sysRole == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysRoleEditReq, sysRole);
        Boolean yesOrNo = iSysRoleService.update(sysRole,new QueryWrapper<SysRole>().lambda().eq(SysRole::getId,sysRole.getId()));
        ExceptionUtils.businessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysRolePermissionService.saveOrDeleteRolePermissionId(sysRole.getId(),sysRoleEditReq.getRolePermissionId(),false,true), CommonEnumConstant.PromptMessage.ADD_ROLE_PERMISSION_ERROR);
        ExceptionUtils.businessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/delete")
    @ApiOperation(httpMethod = "POST",value = "删除角色")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@Validated @RequestBody DeleteKey deleteKey) {
        log.info("delete SysRole start...");
        ExceptionUtils.businessException(!iSysRoleService.removeById(Long.valueOf(deleteKey.getKeyWord().toString())),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysRolePermissionService.saveOrDeleteRolePermissionId(Long.valueOf(deleteKey.getKeyWord().toString()),null,true,false), CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
        ExceptionUtils.businessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/batch-delete")
    @ApiOperation(httpMethod = "POST",value = "批量删除角色")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysRole start...");
        ExceptionUtils.businessException(!iSysRoleService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysRolePermissionService.batchDeleteByRoleIds(batchDeleteKey.getKeyWords()), CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
        ExceptionUtils.businessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
