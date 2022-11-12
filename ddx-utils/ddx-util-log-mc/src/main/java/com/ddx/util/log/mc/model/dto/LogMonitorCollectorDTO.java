package com.ddx.util.log.mc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: LogMonitorCollectorDTO
 * @Description: 日志监控采集配置
 * @Author: YI.LAU
 * @Date: 2022年11月10日 17:33
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMonitorCollectorDTO {

    /**
     * 日志文件读取偏移量
     */
    private Long offset;
}
