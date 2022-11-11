package com.ddx.util.basis.utils;

import com.ddx.util.basis.constant.ConstantUtils;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: RequestContextUtils
 * @Description: Request工具类
 * @author YI.LAU
 * @since 2022-03-15
 * @Version: 1.0
 */
public class RequestContextUtils {


    /**
     * 获取HttpServletRequest
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)(Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))).getRequest();
    }

    /**
     * 将 HttpServletRequest  Header 转成 map
     * @param request
     * @return map
     */
    public static Map<String, String> getRequestHeaderMap(HttpServletRequest request) {
        Map<String, String> header = new HashMap<>(16);
        Enumeration<String> enums = request.getHeaderNames();
        while (enums.hasMoreElements()) {
            String headerParam = enums.nextElement();
            header.put(headerParam, request.getHeader(headerParam));
        }
        return header;
    }

    /**
     * 设置流水号
     */
    public static void settingSerialNumber(){
        HttpServletRequest request = getRequest();
        if (request != null){
            MDC.put(ConstantUtils.REQUEST_SERIAL_NUMBER,RequestContextUtils.getRequestHeaderMap(request).get(ConstantUtils.REQUEST_SERIAL_NUMBER));
        }
    }
}
