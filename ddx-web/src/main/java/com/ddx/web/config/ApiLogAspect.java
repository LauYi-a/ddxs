package com.ddx.web.config;

import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.model.vo.Header;
import com.ddx.util.basis.model.vo.SysApiLogVo;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.DateUtil;
import com.ddx.util.basis.utils.IPUtils;
import com.ddx.util.basis.utils.RequestContextUtils;
import com.ddx.util.basis.utils.StringUtil;
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

    @Value("${ddxlog.isFormatterHttp:false}")
    private Boolean isFormatterHttp;

    /*
     * 请求入口
     * @param joinPoint
     */
    @Before(value = "controllerAspect()")
    public void beforeHandle(JoinPoint joinPoint) {
        HttpServletRequest request = RequestContextUtils.getRequest();
        if (request != null) {
            RequestContextUtils.settingSerialNumber();
            SysApiLogVo sysLogAspectVo = setLogEntityVo(BasisConstant.REQUEST_TYPE_REQ,RequestContextUtils.getRequest(),joinPoint.getArgs());
            log.info("request content is:\n{}", StringUtil.jsonFormatter(sysLogAspectVo,isFormatterHttp));
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
            SysApiLogVo sysLogAspectVo = setLogEntityVo(BasisConstant.REQUEST_TYPE_RESP,request,responseData);
            if (responseData.getType().equals(BasisConstant.MSG_TYPE_INFO)||responseData.getType().equals(BasisConstant.MSG_TYPE_SUCCESS)){
                log.info("response content is:\n{}",  StringUtil.jsonFormatter(sysLogAspectVo,isFormatterHttp));
            }else if (responseData.getType().equals(BasisConstant.MSG_TYPE_WARNING)){
                log.warn("response content is:\n{}",  StringUtil.jsonFormatter(sysLogAspectVo,isFormatterHttp));
            }else if (responseData.getType().equals(BasisConstant.MSG_TYPE_ERROR)){
                log.error("response content is:\n{}", StringUtil.jsonFormatter(sysLogAspectVo,isFormatterHttp));
            }
        }
    }

    private SysApiLogVo setLogEntityVo(String type, HttpServletRequest request, Object data){
        return  SysApiLogVo.builder()
                .data(data)
                .header(Header.builder()
                        .serialNumber(MDC.get(BasisConstant.REQUEST_SERIAL_NUMBER))
                        .url(request.getRequestURL().toString())
                        .type(type)
                        .nickname(UserOauthInfo.getCurrentUser().getNickname())
                        .userId(String.valueOf(UserOauthInfo.getCurrentUser().getUserId()))
                        .ip(IPUtils.getIpAddr(request))
                        .contentType(request.getContentType())
                        .dateTime(DateUtil.date2Str(new Date(), BasisConstant.DATE_FORMAT_13))
                        .build())
                .build();
    }
}