package com.ddx.basis.exception;

import lombok.Data;

/**
 * @ClassName: ErrorBusinessException
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年03月18日
 * @Version: 1.0
 */
@Data
public class ErrorBusinessException extends RuntimeException{


    private String msg;

    public ErrorBusinessException(String msg){
        super(msg);
        this.msg = msg;
    }
}
