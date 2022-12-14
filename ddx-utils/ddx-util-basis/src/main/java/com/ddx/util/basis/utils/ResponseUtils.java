package com.ddx.util.basis.utils;

import com.ddx.util.basis.response.BaseResponse;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: ResponseUtils
 * @Description: 结果封装类
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
public class ResponseUtils {

    public static void resultError(HttpServletResponse response, BaseResponse baseResponse) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        out.write(objectMapper.writeValueAsString(BaseResponse.toMap(baseResponse)).getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
