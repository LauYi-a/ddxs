package com.ddx.log.controller;

import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.log.api.model.req.QueryLogReq;
import com.ddx.log.api.service.ILogApiService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: LogApiController
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年11月04日 13:58
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api")
public class LogApiController implements ILogApiService {


    @Override
    @PostMapping("/select-log")
    @ApiOperation(httpMethod = "POST",value = "查询日志")
    public BaseResponse selectLog(@Validated @RequestBody QueryLogReq queryLogReq) {
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
