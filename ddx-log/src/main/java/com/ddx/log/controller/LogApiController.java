package com.ddx.log.controller;

import com.ddx.log.api.service.ILogApiService;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
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
    public BaseResponse selectLog() {
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,"查询日志");
    }
}