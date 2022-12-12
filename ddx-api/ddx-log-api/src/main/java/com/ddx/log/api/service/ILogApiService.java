package com.ddx.log.api.service;

import com.ddx.util.basis.response.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "ddx-log",path = "/ddx/log/api")
public interface ILogApiService {

    @PostMapping("/select-log")
    @ApiOperation(httpMethod = "POST",value = "查询日志")
    BaseResponse selectLog();
}