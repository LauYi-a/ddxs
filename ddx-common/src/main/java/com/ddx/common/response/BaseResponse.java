package com.ddx.common.response;

import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.slf4j.MDC;

/**
 * @ClassName: BaseResponse
 * @Description: 响应数据基类
 * @Author: YI.LAU
 * @Date: 2022年03月18日  0018
 * @Version: 1.0
 */
@Data
@ApiModel(value = "BaseResponse" ,description = "响应数据基类")
public class BaseResponse {

    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("消息类型")
    private String type;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    @ApiModelProperty("流水号")
    private String serialNumber;

    @ApiModelProperty("响应消息")
    private String msg;


    protected BaseResponse() {}

    protected BaseResponse(CommonEnumConstant.PromptMessage code) {
        this.code = code.getCode();
        this.type = code.getType();
        this.msg = code.getMsg();
        this.serialNumber = MDC.get(ConstantUtils.REQUEST_SERIAL_NUMBER);
    }

    protected BaseResponse(Integer code,String type,String msg) {
        this.code = code;
        this.type = type;
        this.msg = msg;
        this.serialNumber = MDC.get(ConstantUtils.REQUEST_SERIAL_NUMBER);
    }

    protected BaseResponse(CommonEnumConstant.PromptMessage code,String msg) {
        this.code = code.getCode();
        this.type = code.getType();
        this.msg = msg;
        this.serialNumber = MDC.get(ConstantUtils.REQUEST_SERIAL_NUMBER);
    }
}
