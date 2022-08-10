package com.ddx.common.exception;

import com.ddx.common.constant.CommonEnumConstant;

/**
 * @ClassName: ExceptionUtils
 * @Description: 异常构造工具类
 * @Author: YI.LAU
 * @Date: 2022年03月18日
 * @Version: 1.0
 */
public class ExceptionUtils {

    ExceptionUtils(){}

    /**
     * 抛出带错误码自定义信息异常
     * @param promptMessage
     * @param message
     */
    public static void businessException(CommonEnumConstant.PromptMessage promptMessage,Object message){
        throw new ErrorBusinessException(promptMessage.getCode(), promptMessage.getType(),String.format(promptMessage.getMsg(),message));
    }

    /**
     * 满足条件时抛出带错误码自定义信息异常
     * @param promptMessage
     * @param condition
     * @param message
     */
    public static void businessException(Boolean condition, CommonEnumConstant.PromptMessage promptMessage, Object message){
        if (condition){
            throw new ErrorBusinessException(promptMessage.getCode(), promptMessage.getType(),String.format(promptMessage.getMsg(),message));
        }
    }

    /**
     * 满足条件时抛出异常 带错误码
     * @param condition
     * @param promptMessage
     */
    public static void errorBusinessException(Boolean condition, CommonEnumConstant.PromptMessage promptMessage){
        if (condition){
            throw new ErrorBusinessException(promptMessage.getCode(), promptMessage.getType(),promptMessage.getMsg());
        }
    }

    /**
     * 抛出错误码业务异常 带错误码
     * @param promptMessage
     */
    public static void errorBusinessException(CommonEnumConstant.PromptMessage promptMessage){
        throw new ErrorBusinessException(promptMessage.getCode(), promptMessage.getType(),promptMessage.getMsg());
    }

}
