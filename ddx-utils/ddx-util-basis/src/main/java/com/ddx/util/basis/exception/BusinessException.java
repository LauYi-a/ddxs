package com.ddx.util.basis.exception;

import com.ddx.util.basis.constant.CommonEnumConstant;
import lombok.Data;

/**
 * @ClassName: BusinessException
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年03月18日
 * @Version: 1.0
 */
@Data
public class BusinessException extends RuntimeException{

    private Integer code;
    private String type;
    private String msg;

    public BusinessException(Integer code, String type, String msg){
        super(msg);
        this.code = code;
        this.type = type;
        this.msg = msg;
    }

    public BusinessException(CommonEnumConstant.PromptMessage promptMessage){
        super(promptMessage.getMsg());
        this.code = promptMessage.getCode();
        this.type = promptMessage.getType();
        this.msg = promptMessage.getMsg();
    }
}
