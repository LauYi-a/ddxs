package com.ddx.basis.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SysLogAspectVo
 * @Description: 系统请求日志切面
 * @Author: YI.LAU
 * @Date: 2022年04月27日
 * @Version: 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLogAspectVo {

    /**
     * 请求流水号
     */
    private String serialNumber;

    /**
     * 日志类型
     */
    private String type;

    /**
     * 请求路由
     */
    private String url;

    /**
     * 请求 header
     */
    private Object header;

    /**
     * 请求返回参数
     */
    private Object param;

    /**
     * 请求用户IP
     */
    private String ip;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 用户ID
     */
    private String userId;

}
