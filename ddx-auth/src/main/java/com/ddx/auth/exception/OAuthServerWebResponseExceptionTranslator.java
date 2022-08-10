package com.ddx.auth.exception;

import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.exception.ErrorBusinessException;
import com.ddx.common.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;


/**
 * @ClassName: OAuthServerWebResponseExceptionTranslator
 * @Description: 自定义异常翻译器，针对用户名、密码异常，授权类型不支持的异常进行处理
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
public class OAuthServerWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    /**
     * 业务处理方法，重写这个方法返回客户端信息
     */
    @Override
    public ResponseEntity translate(Exception e){

        if(e instanceof UnsupportedGrantTypeException){
            //判断异常，不支持的认证方式
            return ResponseEntity.ok(ResponseData.out(CommonEnumConstant.PromptMessage.UNSUPPORTED_GRANT_TYPE));
        }else if(e instanceof InvalidGrantException){
            //用户名或密码异常
            return ResponseEntity.ok(ResponseData.out(CommonEnumConstant.PromptMessage.USERNAME_OR_PASSWORD_ERROR));
        }else if (e instanceof InternalAuthenticationServiceException) {
            //身份验证失败
            try {
                ErrorBusinessException ex = (ErrorBusinessException) e.getCause();
                return ResponseEntity.ok(ResponseData.out(ex.getCode(),ex.getType(),ex.getMsg()));
            }catch (Exception e1){
                return ResponseEntity.ok(ResponseData.out(CommonEnumConstant.PromptMessage.ID_AUTHENTICATION_FAILED,CommonEnumConstant.PromptMessage.ID_AUTHENTICATION_FAILED+ ConstantUtils.COLON+e.getMessage()));
            }
        } else if (e instanceof InvalidTokenException) {
            //Token无效或过期
            return ResponseEntity.ok(ResponseData.out(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
        }
        return ResponseEntity.ok(ResponseData.out(CommonEnumConstant.PromptMessage.SYS_ERROR));
    }
}
