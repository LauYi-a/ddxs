package com.ddx.basis.exception;

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
}
