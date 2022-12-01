package com.ddx.sys.controller;

import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.es.service.IIndexManageServiceApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: LogController
 * @Description: 日志控制器
 * @Author: YI.LAU
 * @Date: 2022年04月09日
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/log")
@Api(tags = "日志")
public class LogController {

    @Autowired
    private IIndexManageServiceApi iIndexManageServiceApi;

    @PostMapping("/select-log")
    @ApiOperation(httpMethod = "POST",value = "查询日志")
    public BaseResponse getInfo(){
        try {
            List<String> aliasAll = iIndexManageServiceApi.selectAliasAll();
            List<String> indexAll = iIndexManageServiceApi.selectIndexAll();
            System.out.println(aliasAll);
            System.out.println(indexAll);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.out(CommonEnumConstant.PromptMessage.FAILED);
        }
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
