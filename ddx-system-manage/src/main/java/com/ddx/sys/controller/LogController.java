package com.ddx.sys.controller;

import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.utils.IPUtils;
import com.ddx.common.service.impl.RedisLockServiceImpl;
import com.ddx.log.api.model.req.QueryLogReq;
import com.ddx.log.api.service.ILogApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private RedisLockServiceImpl redisLockServiceImpl;
    @Autowired
    private ILogApiService logApiService;
    @Value("${spring.application.name}")
    private String serviceName;

    @PostMapping("/select-log")
    @ApiOperation(httpMethod = "POST",value = "查询日志")
    public BaseResponse getInfo(){
        BaseResponse longValue = redisLockServiceImpl.tryLockException("select:log:", () -> {
           return logApiService.selectLog(QueryLogReq.builder().serviceName(serviceName).cpIp(IPUtils.getIp()).build());
        });
        return longValue;
    }
}
