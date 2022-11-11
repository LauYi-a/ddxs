package com.ddx.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.basis.model.req.BatchDeleteKey;
import com.ddx.basis.model.req.DeleteKey;
import com.ddx.basis.model.resp.PaginatedResult;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.PageUtil;
import com.ddx.sys.entity.SysWhitelistRequest;
import com.ddx.sys.model.req.sysWhitelistRequest.SysWhitelistRequestAddReq;
import com.ddx.sys.model.req.sysWhitelistRequest.SysWhitelistRequestEditReq;
import com.ddx.sys.model.req.sysWhitelistRequest.SysWhitelistRequestQueryReq;
import com.ddx.sys.service.ISysWhitelistRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: SysWhitelistRequestController
 * @Description: 白名单路由 前端控制器
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-whitelist-request")
@Api(tags = "白名单路由控制层")
public class SysWhitelistRequestController {

    @Autowired
    private ISysWhitelistRequestService iSysWhitelistRequestService;

    @PostMapping("/list")
    @ApiOperation(httpMethod = "POST",value = "查询白名单路由列表")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysWhitelistRequestQueryReq sysWhitelistRequestQueryReq) {
        log.info("get SysWhitelistRequest list..");
        int page = PageUtil.parsePage(sysWhitelistRequestQueryReq.getPage(), ConstantUtils.PAGE);
        int perPage = PageUtil.parsePerPage(sysWhitelistRequestQueryReq.getPerPage(), ConstantUtils.PER_PAGE);
        IPage<SysWhitelistRequest> dataList=iSysWhitelistRequestService.selectPage(new Page<SysWhitelistRequest>(page, perPage),new QueryWrapper<SysWhitelistRequest>().lambda()
        .eq(StringUtils.isNoneBlank(sysWhitelistRequestQueryReq.getServiceModule()),SysWhitelistRequest::getServiceModule, sysWhitelistRequestQueryReq.getServiceModule())
        .eq(StringUtils.isNoneBlank(sysWhitelistRequestQueryReq.getType()),SysWhitelistRequest::getType,sysWhitelistRequestQueryReq.getType())
        .like(StringUtils.isNoneBlank(sysWhitelistRequestQueryReq.getName()),SysWhitelistRequest::getName, sysWhitelistRequestQueryReq.getName())
        .like(StringUtils.isNoneBlank(sysWhitelistRequestQueryReq.getUrl()),SysWhitelistRequest::getUrl, sysWhitelistRequestQueryReq.getUrl())
        .orderByDesc(SysWhitelistRequest::getUpdateTime));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, PaginatedResult.builder()
        .resultData(dataList.getRecords())
        .currentPage((int) dataList.getCurrent())
        .totalCount( dataList.getTotal())
        .totalPage((int) dataList.getPages())
        .build());
    }

    @PostMapping("/add")
    @ApiOperation(httpMethod = "POST",value = "添加白名单路由")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(@Validated @RequestBody SysWhitelistRequestAddReq  sysWhitelistRequestAddReq) {
        log.info("add SysWhitelistRequest start..");
        SysWhitelistRequest sysWhitelistRequest = new SysWhitelistRequest();
        BeanUtils.copyProperties(sysWhitelistRequestAddReq,sysWhitelistRequest);
        ExceptionUtils.businessException(iSysWhitelistRequestService.count(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getUrl,sysWhitelistRequest.getUrl())
                        .eq(SysWhitelistRequest::getType,sysWhitelistRequestAddReq.getType()))>0,CommonEnumConstant.PromptMessage.WHITELIST_ERROR);
        ExceptionUtils.businessException(!iSysWhitelistRequestService.save(sysWhitelistRequest),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysWhitelistRequestService.initWhitelistConfig(),CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/edit")
    @ApiOperation(httpMethod = "POST",value = "修改白名单路由")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysWhitelistRequestEditReq sysWhitelistRequestEditReq) {
        log.info("edit SysWhitelistRequest start...");
        SysWhitelistRequest sysWhitelistRequest = iSysWhitelistRequestService.getOne(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getId,sysWhitelistRequestEditReq.getId()).last("limit 1"));
        ExceptionUtils.businessException(sysWhitelistRequest == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysWhitelistRequestEditReq, sysWhitelistRequest);
        Boolean yesOrNo = iSysWhitelistRequestService.update(sysWhitelistRequest,new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getId,sysWhitelistRequest.getId()));
        ExceptionUtils.businessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysWhitelistRequestService.initWhitelistConfig(),CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/delete")
    @ApiOperation(httpMethod = "POST",value = "删除白名单路由")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@Validated @RequestBody DeleteKey deleteKey) {
        log.info("delete SysWhitelistRequest start...");
        ExceptionUtils.businessException(!iSysWhitelistRequestService.removeById(Long.valueOf(deleteKey.getKeyWord().toString())),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysWhitelistRequestService.initWhitelistConfig(),CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/batch-delete")
    @ApiOperation(httpMethod = "POST",value = "批量删除白名单路由")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysWhitelistRequest start...");
        ExceptionUtils.businessException(!iSysWhitelistRequestService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysWhitelistRequestService.initWhitelistConfig(),CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
