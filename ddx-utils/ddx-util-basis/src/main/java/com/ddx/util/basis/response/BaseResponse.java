package com.ddx.util.basis.response;

import com.ddx.util.basis.constant.ConstantUtils;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.MDC;

/**
 * @ClassName: BaseResponse
 * @Description: 响应数据基类
 * @Author: YI.LAU
 * @Date: 2022年03月18日  0018
 * @Version: 1.0
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@ApiModel(value = "BaseResponse" ,description = "响应数据基类")
public class BaseResponse {

    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("消息类型")
    private String type;

    @ApiModelProperty("流水号")
    private String serialNumber;

    @ApiModelProperty("响应消息")
    private String msg;


    protected BaseResponse() {}

    protected BaseResponse(CommonEnumConstant.PromptMessage enums) {
        this.code = enums.getCode();
        this.type = enums.getType();
        this.msg = enums.getMsg();
        this.serialNumber = MDC.get(ConstantUtils.REQUEST_SERIAL_NUMBER);
    }

    protected BaseResponse(Integer code,String type,String msg) {
        this.code = code;
        this.type = type;
        this.msg = msg;
        this.serialNumber = MDC.get(ConstantUtils.REQUEST_SERIAL_NUMBER);
    }

    protected BaseResponse(CommonEnumConstant.PromptMessage enums,String msg) {
        this.code = enums.getCode();
        this.type = enums.getType();
        this.msg = msg;
        this.serialNumber = MDC.get(ConstantUtils.REQUEST_SERIAL_NUMBER);
    }
}
