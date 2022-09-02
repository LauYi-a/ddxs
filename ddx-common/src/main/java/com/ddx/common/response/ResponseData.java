package com.ddx.common.response;

import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @ClassName: ResponseData
 * @Description: 响应数据体
 * @Author: YI.LAU
 * @Date: 2022年03月18日  0018
 * @Version: 1.0
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ResponseData" ,description = "响应数据体")
public class ResponseData<T> extends BaseResponse {

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    @ApiModelProperty("返回数据")
    private T data;

    private ResponseData(CommonEnumConstant.PromptMessage code) {
        super(code);
    }

    private ResponseData(CommonEnumConstant.PromptMessage code, T data) {
        super(code);
        this.data = data;
    }

    /**
     * 对外基础响应体已供调用接口操作
     * @param code
     * @return
     */
    public static BaseResponse out(Integer code,String type,String messger) {
        return new BaseResponse(code,type,messger);
    }

    /**
     * 对外基础响应体已供调用接口操作
     * @param code
     * @return
     */
    public static BaseResponse out(CommonEnumConstant.PromptMessage code) {
        return new BaseResponse(code);
    }

    /**
     * 对外基础响应体已供调用接口操作 自定义错误信息
     * @param code
     * @return
     */
    public static BaseResponse out(CommonEnumConstant.PromptMessage code,String messger) {
        return new BaseResponse(code,messger);
    }

    /**
     * 对外开放数据响应体已供调用，引用了范型设计，支持各种数据类型
     * @param code
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseData<T> out(CommonEnumConstant.PromptMessage code, T data) {
        return new ResponseData<>(code, data);
    }

    /**
     * 判断答应状态是否成功
     * 可供系统内部远程调用判断
     * @param responseData
     * @return Boolean true 调用成功返回 false 失败返回
     */
    public static Boolean isSuccess(ResponseData responseData){
        return Objects.equals(responseData.getType(), ConstantUtils.MSG_TYPE_SUCCESS)||Objects.equals(responseData.getType(), ConstantUtils.MSG_TYPE_INFO);
    }

    /**
     * 重载 基础返回体 判断答应状态是否成功
     * 可供系统内部远程调用判断
     * @param baseResponse
     * @return Boolean true 调用成功返回 false 失败返回
     */
    public static Boolean isSuccess(BaseResponse baseResponse){
        return Objects.equals(baseResponse.getType(), ConstantUtils.MSG_TYPE_SUCCESS)||Objects.equals(baseResponse.getType(), ConstantUtils.MSG_TYPE_INFO);
    }
}
