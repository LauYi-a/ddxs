package com.ddx.basis.response;

import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.enums.CommonEnumConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Objects;

/**
 * @ClassName: ResponseData
 * @Description: 响应数据体
 * @Author: YI.LAU
 * @Date: 2022年03月18日  0018
 * @Version: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ResponseData" ,description = "响应数据体")
public class ResponseData<T> extends BaseResponse {

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    @ApiModelProperty("返回数据")
    private T data;

    /**
     * 返回枚举信息
     * @param enums
     */
    private ResponseData(CommonEnumConstant.PromptMessage enums){
        super(enums);
    }

    /**
     * 返回枚举自定义信息
     * @param enums
     * @param message
     */
    private ResponseData(CommonEnumConstant.PromptMessage enums,String message) {
        super(enums,message);
    }

    /**
     * 返回 自定义编码类型信息
     * @param code
     * @param type
     * @param message
     */
    private ResponseData(Integer code,String type,String message){
        super(code,type,message);
    }

    /**
     * 返回枚举 数据
     * @param enums
     * @param data
     */
    private ResponseData(CommonEnumConstant.PromptMessage enums, T data) {
        super(enums);
        this.data = data;
    }


    /**
     * 对外基础响应体
     * 供调用接口操作
     * @param enums
     * @return 返回枚举信息
     */
    public static ResponseData out(CommonEnumConstant.PromptMessage enums) {
        return new ResponseData(enums);
    }

    /**
     * 对外基础响应体 供调用接口操作
     * @param code
     * @return 返回 自定义编码类型信息
     */
    public static ResponseData out(Integer code,String type,String message) {
        return new ResponseData(code,type,message);
    }

    /**
     * 对外响应体已供调用接口操作
     * @param enums
     * @param message
     * @return 返回枚举编码类型 自定义信息
     */
    public static  ResponseData out(CommonEnumConstant.PromptMessage enums, String message) {
        return new ResponseData(enums, message);
    }

    /**
     * 对外开放数据响应体供调用接口操作
     * 引用了范型设计，支持各种数据类型
     * @param enums
     * @param data
     * @param <T>
     * @return 返回枚举编码类型信息 数据
     */
    public static <T> ResponseData<T> out(CommonEnumConstant.PromptMessage enums, T data) {
        return new ResponseData<>(enums, data);
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
     * 重载 基础响应返回体
     * 判断答应状态是否成功
     * 可供系统内部远程调用判断
     * @param baseResponse
     * @return Boolean true 调用成功返回 false 失败返回
     */
    public static Boolean isSuccess(BaseResponse baseResponse){
        return Objects.equals(baseResponse.getType(), ConstantUtils.MSG_TYPE_SUCCESS)||Objects.equals(baseResponse.getType(), ConstantUtils.MSG_TYPE_INFO);
    }
}
