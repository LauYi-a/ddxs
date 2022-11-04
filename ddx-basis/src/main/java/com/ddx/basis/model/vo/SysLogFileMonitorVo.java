package com.ddx.basis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SysLogFileMonitorVo
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年10月31日 18:22
 * @Version: 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLogFileMonitorVo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 主机IP
     */
    private String ip;

    /**
     * 日志数据
     */
    private String data;
}
