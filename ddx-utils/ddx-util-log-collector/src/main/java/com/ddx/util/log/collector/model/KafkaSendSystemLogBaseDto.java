package com.ddx.util.log.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ElasticsearchBaseDto
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年11月22日 11:20
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaSendSystemLogBaseDto {

    public String id;

    public String serviceName;

    public String env;

    public String date;

    public String message;

}
