package com.ddx.basis.exception;

import com.ddx.basis.enums.CommonEnumConstant;

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
        String enumMessage = promptMessage.getMsg().contains("%s")?promptMessage.getMsg():promptMessage.getMsg()+"：%s";
        throw new BusinessException(promptMessage.getCode(), promptMessage.getType(),String.format(enumMessage,message));
    }

    /**
     * 满足条件时抛出带错误码自定义信息异常
     * @param promptMessage
     * @param condition
     * @param message
     */
    public static void businessException(Boolean condition, CommonEnumConstant.PromptMessage promptMessage, Object message){
        if (condition){
            throw new BusinessException(promptMessage.getCode(), promptMessage.getType(),String.format(promptMessage.getMsg(),message));
        }
    }

    /**
     * 满足条件时抛出异常 带错误码
     * @param condition
     * @param promptMessage
     */
    public static void errorBusinessException(Boolean condition, CommonEnumConstant.PromptMessage promptMessage){
        if (condition){
            throw new BusinessException(promptMessage.getCode(), promptMessage.getType(),promptMessage.getMsg());
        }
    }

    /**
     * 抛出错误码业务异常 带错误码
     * @param promptMessage
     */
    public static void errorBusinessException(CommonEnumConstant.PromptMessage promptMessage){
        throw new BusinessException(promptMessage.getCode(), promptMessage.getType(),promptMessage.getMsg());
    }

    /**
     * 满足条件抛出指定错误信息
     * @param condition
     * @param message
     */
    public static void errorBusinessException(Boolean condition,String message ){
        if (condition){
            throw new ErrorBusinessException(message);
        }
    }
}
