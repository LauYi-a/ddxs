package com.ddx.sys.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.common.utils.PageUtil;
import com.ddx.sys.dto.req.sysAspectLog.SysAspectLogQueryReq;
import com.ddx.sys.entity.SysAspectLog;
import com.ddx.sys.service.ISysAspectLogService;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.dto.req.BatchDeleteKey;
import com.ddx.common.dto.resp.PaginatedResult;
import com.ddx.common.exception.ExceptionUtils;
import com.ddx.common.response.BaseResponse;
import com.ddx.common.response.ResponseData;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @ClassName: SysAspectLogController
 * @Description: 切面日志 前端控制器
 * @author YI.LAU
 * @since 2022-04-29
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-aspect-log")
@Api(tags = "切面日志控制层")
public class SysAspectLogController {

    @Autowired
    private ISysAspectLogService iSysAspectLogService;

    @PostMapping("/list")
    @ApiOperation(value = "查询切面日志列表", notes = "切面日志")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysAspectLogQueryReq sysAspectLogQueryReq) {
        log.info("sys-aspect-log getList list..");
        int page = PageUtil.parsePage(sysAspectLogQueryReq.getPage(), ConstantUtils.PAGE);
        int perPage = PageUtil.parsePerPage(sysAspectLogQueryReq.getPerPage(), ConstantUtils.PER_PAGE);
        IPage<SysAspectLog> dataList=iSysAspectLogService.selectPage(new Page<SysAspectLog>(page, perPage),new QueryWrapper<SysAspectLog>().lambda()
        .eq(StringUtils.isNoneBlank(sysAspectLogQueryReq.getSerialNumber()),SysAspectLog::getSerialNumber, sysAspectLogQueryReq.getSerialNumber())
        .eq(StringUtils.isNoneBlank(sysAspectLogQueryReq.getUserId()),SysAspectLog::getUserId, sysAspectLogQueryReq.getUserId())
        .like(StringUtils.isNoneBlank(sysAspectLogQueryReq.getUrl()),SysAspectLog::getUrl, sysAspectLogQueryReq.getUrl())
        .orderByDesc(SysAspectLog::getUpdateTime));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, PaginatedResult.builder()
        .resultData(dataList.getRecords())
        .currentPage((int) dataList.getCurrent())
        .totalCount( dataList.getTotal())
        .totalPage((int) dataList.getPages())
        .build());
    }

    @ApiOperation(value = "批量删除切面日志", notes = "切面日志")
    @PostMapping("/batch-delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysAspectLog start...");
        ExceptionUtils.errorBusinessException(!iSysAspectLogService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
