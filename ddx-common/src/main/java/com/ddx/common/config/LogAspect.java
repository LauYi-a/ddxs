package com.ddx.common.config;

import com.alibaba.fastjson.JSONObject;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.dto.vo.SysLogAspectVo;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.IPUtils;
import com.ddx.basis.utils.RequestContextUtils;
import com.ddx.basis.utils.StringUtil;
import com.ddx.common.mq.IProducer;
import com.ddx.common.utils.OauthUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;



/**
 * @ClassName: LogAspect
 * @Description: 请求响应日志切面
 * 通过kafka发送消息到消费入库
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private IProducer producer;

    @Pointcut("execution(* com.ddx.*.controller.*.*(..))")
    private void controllerAspect() {}

    /*
     * 请求入口
     * @param joinPoint
     */
    @Before(value = "controllerAspect()")
    public void beforeHandle(JoinPoint joinPoint) {
        HttpServletRequest request = RequestContextUtils.getRequest();
        if (request != null) {
            RequestContextUtils.settingSerialNumber();
            SysLogAspectVo sysLogAspectVo = setLogEntityVo(ConstantUtils.REQUEST_TYPE_REQ,RequestContextUtils.getRequest(),joinPoint.getArgs());
            //请求日志占时不存储
            //producer.send(sysLogAspectVo, UUIDUtil.getUUID(), ConstantUtils.SEND_LOG_ASPECT_TOPIC);
            log.info(" \n request content is: {}", StringUtil.jsonFormatter(sysLogAspectVo));
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
            SysLogAspectVo sysLogAspectVo = setLogEntityVo(ConstantUtils.REQUEST_TYPE_RESP,request,responseData);
            //请求日志占时不存储
            //producer.send(sysLogAspectVo, UUIDUtil.getUUID(), ConstantUtils.SEND_LOG_ASPECT_TOPIC);
            if (responseData.getType().equals(ConstantUtils.MSG_TYPE_INFO)||responseData.getType().equals(ConstantUtils.MSG_TYPE_SUCCESS)){
                sysLogAspectVo.setParam(JSONObject.toJSONString(responseData));
                log.info("\n response content is: {}",  StringUtil.jsonFormatter(sysLogAspectVo));
            }else if (responseData.getType().equals(ConstantUtils.MSG_TYPE_WARNING)){
                log.warn("\n response content is: {}",  StringUtil.jsonFormatter(sysLogAspectVo));
            }else if (responseData.getType().equals(ConstantUtils.MSG_TYPE_ERROR)){
                log.error("\n response content is: {}", StringUtil.jsonFormatter(sysLogAspectVo));
            }
        }
    }

    private SysLogAspectVo setLogEntityVo(String type,HttpServletRequest request,Object param){
        return  SysLogAspectVo.builder()
                .serialNumber(MDC.get(ConstantUtils.REQUEST_SERIAL_NUMBER))
                .url(request.getRequestURL().toString())
                .header(RequestContextUtils.getRequestHeaderMap(request))
                .type(type)
                .param(param)
                .nickname(OauthUtils.getCurrentUser().getNickname())
                .userId(OauthUtils.getCurrentUser().getUserId())
                .ip(IPUtils.getIpAddr(request))
                .build();
    }
}