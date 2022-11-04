package com.ddx.log.api.service;

import com.ddx.basis.response.BaseResponse;
import com.ddx.log.api.model.req.QueryLogReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName: ILogApiService
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年04月09日
 * @Version: 1.0
 */

@FeignClient(value = "ddx-log",path = "/ddx/log/api")
public interface ILogApiService {

    @PostMapping("/select-log")
    @ApiOperation(httpMethod = "POST",value = "查询日志")
    BaseResponse selectLog(@Validated @RequestBody QueryLogReq queryLogReq);
}
