package com.ddx.sys.controller;

import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.response.ResponseData;
import com.ddx.common.service.impl.RedisLockServiceImpl;
import com.ddx.test.service.ITestApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: OpenFeignTestController
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年04月09日
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "测试 openfeign API")
public class OpenFeignTestController {

    @Autowired
    private ITestApiService iTestApiService;
    @Autowired
    private RedisLockServiceImpl redisLockServiceImpl;

    @PostMapping("/get-info")
    @ApiOperation(value = "系统参数查询", notes = "系统参数")
    public ResponseData getInfo(){
        ResponseData longValue = redisLockServiceImpl.tryLockException("order:pay:", () -> {
            return iTestApiService.getTestName();
        });

        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,longValue);
    }
}
