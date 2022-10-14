package com.ddx.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.sys.dto.req.sysResource.SysResourceEditReq;
import com.ddx.sys.dto.req.sysResource.SysResourceQueryReq;
import com.ddx.sys.dto.req.sysUser.QueryUserInfoReq;
import com.ddx.sys.dto.resp.sysResource.MenuTreeListResp;
import com.ddx.sys.dto.resp.sysResource.ServiceMenuResp;
import com.ddx.sys.entity.SysResource;
import com.ddx.sys.service.ISysResourceService;
import com.ddx.sys.service.ISysUserResourceService;
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
 * @ClassName: SysResourceController
 * @Description: 系统资源 前端控制器
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-resource")
@Api(tags = "系统资源控制层")
public class SysResourceController {

    @Autowired
    private ISysResourceService iSysResourceService;
    @Autowired
    private ISysUserResourceService iSysUserResourceService;

    @PostMapping("/select-user-resource-ids")
    @ApiOperation(value = "查询用户菜单资源ID", notes = "系统资源")
    public ResponseData<List<Long>> selectUserResourceIds(@Validated @RequestBody QueryUserInfoReq queryUserInfoReq) {
        log.info("select user response ids ...");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysUserResourceService.selectUserResourceIdsByUserId(queryUserInfoReq.getId()));
    }

    @PostMapping("/select-menu-tree")
    @ApiOperation(value = "查询资源菜单树", notes = "系统资源")
    public ResponseData<List<ServiceMenuResp>> selectMenuTree() {
        log.info("select Response menu all...");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysResourceService.selectMenuTree());
    }

    @PostMapping("/select-menu-tree-list")
    @ApiOperation(value = "查询系统资源树列表", notes = "系统资源")
    public ResponseData<List<MenuTreeListResp>> selectMenuTreeList(@Validated @RequestBody SysResourceQueryReq sysResourceQueryReq) {
        log.info("select response menu tree list ...");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,iSysResourceService.selectMenuTreeList(sysResourceQueryReq));
    }


    @ApiOperation(value = "修改系统资源", notes = "系统资源")
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysResourceEditReq sysResourceEditReq) {
        log.info("edit SysResource start...");
        SysResource sysResource = iSysResourceService.getOne(new QueryWrapper<SysResource>().lambda().eq(SysResource::getId,sysResourceEditReq.getId()).last("limit 1"));
        ExceptionUtils.errorBusinessException(sysResource == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysResourceEditReq, sysResource);
        Boolean yesOrNo = iSysResourceService.update(sysResource,new QueryWrapper<SysResource>().lambda().eq(SysResource::getId,sysResource.getId()));
        ExceptionUtils.errorBusinessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
