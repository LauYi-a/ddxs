package com.ddx.common.config;

import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.model.vo.Header;
import com.ddx.basis.model.vo.SysApiLogVo;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.DateUtil;
import com.ddx.basis.utils.IPUtils;
import com.ddx.basis.utils.RequestContextUtils;
import com.ddx.basis.utils.StringUtil;
import com.ddx.common.utils.OauthUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * @ClassName: LogAspect
 * @Description: 请求响应日志切面
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class ApiLogAspect {

    @Pointcut("execution(* com.ddx.*.controller.*.*(..))")
    private void controllerAspect() {}

    @Value("${logging.isFormatterLog:}")
    private Boolean isFormatterLog;

    /*
     * 请求入口
     * @param joinPoint
     */
    @Before(value = "controllerAspect()")
    public void beforeHandle(JoinPoint joinPoint) {
        HttpServletRequest request = RequestContextUtils.getRequest();
        if (request != null) {
            RequestContextUtils.settingSerialNumber();
            SysApiLogVo sysLogAspectVo = setLogEntityVo(ConstantUtils.REQUEST_TYPE_REQ,RequestContextUtils.getRequest(),joinPoint.getArgs());
            log.info("request content is:\n{}", StringUtil.jsonFormatter(sysLogAspectVo,isFormatterLog));
        }
    }

    /*
     * 请求出口
     * @param response
     */
    @AfterReturning(returning = "response", value = "controllerAspect()")
    public void afterHandle(Object response) {
        HttpServletRequest request = RequestContextUtils.getRequest();
        if (response != null) {
            ResponseData responseData = new ResponseData();
            try {
                responseData = (ResponseData) response;
            }catch (Exception e){
                BaseResponse baseResponse = (BaseResponse) response;
                responseData.setCode(baseResponse.getCode());
                responseData.setType(baseResponse.getType());
                responseData.setSerialNumber(baseResponse.getSerialNumber());
                responseData.setMsg(baseResponse.getMsg());
            }
            SysApiLogVo sysLogAspectVo = setLogEntityVo(ConstantUtils.REQUEST_TYPE_RESP,request,responseData);
            if (responseData.getType().equals(ConstantUtils.MSG_TYPE_INFO)||responseData.getType().equals(ConstantUtils.MSG_TYPE_SUCCESS)){
                log.info("response content is:\n{}",  StringUtil.jsonFormatter(sysLogAspectVo,isFormatterLog));
            }else if (responseData.getType().equals(ConstantUtils.MSG_TYPE_WARNING)){
                log.warn("response content is:\n{}",  StringUtil.jsonFormatter(sysLogAspectVo,isFormatterLog));
            }else if (responseData.getType().equals(ConstantUtils.MSG_TYPE_ERROR)){
                log.error("response content is:\n{}", StringUtil.jsonFormatter(sysLogAspectVo,isFormatterLog));
            }
        }
    }

    private SysApiLogVo setLogEntityVo(String type, HttpServletRequest request, Object result){
        return  SysApiLogVo.builder()
                .result(result)
                .header(Header.builder()
                        .serialNumber(MDC.get(ConstantUtils.REQUEST_SERIAL_NUMBER))
                        .url(request.getRequestURL().toString())
                        .type(type)
                        .nickname(OauthUtils.getCurrentUser().getNickname())
                        .userId(OauthUtils.getCurrentUser().getUserId())
                        .ip(IPUtils.getIpAddr(request))
                        .contentType(request.getContentType())
                        .dateTime(DateUtil.date2Str(new Date(),ConstantUtils.DATE_FORMAT_8))
                        .build())
                .build();
    }
}