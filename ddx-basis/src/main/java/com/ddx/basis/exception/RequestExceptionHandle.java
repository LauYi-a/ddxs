package com.ddx.basis.exception;

import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.RequestContextUtils;
import com.netflix.client.ClientException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * @ClassName: RequestValidateExceptionHandle
 * @Description: 统一提示信息校验统一处理异常
 * 如果校验不通过，就在异常中处理，统一输出格式
 * @Author: YI.LAU
 * @Date: 2022年03月30日
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class RequestExceptionHandle {

    /**
     * 校验错误拦截处理
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse validationBodyException(MethodArgumentNotValidException exception) {
        RequestContextUtils.settingSerialNumber();
        BindingResult result = exception.getBindingResult();
        String messger = "";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors != null) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                    log.error("Data check failure : object{" + fieldError.getObjectName() + "},field{" + fieldError.getField() + "},errorMessage{" + fieldError.getDefaultMessage() + "}");
                });
                if (errors.size() > 0) {
                    FieldError fieldError = (FieldError) errors.get(0);
                    messger = fieldError.getDefaultMessage();
                }
            }
        }
        return ResponseData.out(CommonEnumConstant.PromptMessage.PARAMETER_ILLEGAL_ERROR,messger);
    }

    /**
     * 系统异常
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse exception(Exception exception) {
        RequestContextUtils.settingSerialNumber();
        exception.printStackTrace();
        return ResponseData.out(CommonEnumConstant.PromptMessage.SYS_ERROR,exception.getMessage());
    }

    /**
     * 空指针异常
     * @param nullPointerException
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public BaseResponse nullPointerException(NullPointerException nullPointerException){
        RequestContextUtils.settingSerialNumber();
        nullPointerException.printStackTrace();
        return ResponseData.out(CommonEnumConstant.PromptMessage.SYS_NULL_POINTER_ERROR);
    }

    /**
     * 自定义业务异常响应
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException ex){
        return ResponseData.out(ex.getCode(),ex.getType(),ex.getMsg());
    }

    /**
     * 客户端连接失败
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(ClientException.class)
    public BaseResponse exception(ClientException exception) {
        exception.printStackTrace();
        return ResponseData.out(CommonEnumConstant.PromptMessage.CLIENT_FAILED);
    }

    /**
     * 客户端连接失败
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(RetryableException.class)
    public BaseResponse exception(RetryableException exception) {
        RequestContextUtils.settingSerialNumber();
        exception.printStackTrace();
        String msg = "";
        try {
            String[] msgs = exception.getMessage().split("http:");
            msg += "http:"+msgs[1];
        }catch (Exception e){
            String[] msgs = exception.getMessage().split("https:");
            msg += "https:"+msgs[1];
        }
        return ResponseData.out(CommonEnumConstant.PromptMessage.API_READ_TIMED_OUT_ERROR,String.format(CommonEnumConstant.PromptMessage.API_READ_TIMED_OUT_ERROR.getMsg(),msg));
    }

}