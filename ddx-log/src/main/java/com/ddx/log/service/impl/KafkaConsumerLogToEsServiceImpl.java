package com.ddx.log.service.impl;

import com.alibaba.fastjson.JSON;
import com.ddx.log.model.LogCollectorDto;
import com.ddx.log.service.IKafkaConsumerLogToEsService;
import com.ddx.util.es.common.EsUtils;
import com.ddx.util.es.service.IElasticsearchServiceApi;
import com.ddx.util.es.service.IIndexManageServiceApi;
import com.ddx.util.log.collector.model.SendLogToKafkaBaseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @ClassName: KafkaConsumerLogToEsServiceImpl
 * @Description: 消费日志到es存储
 * @Author: YI.LAU
 * @Date: 2022年11月18日 14:37
 * @Version: 1.0
 */
@Slf4j
@Service
public class KafkaConsumerLogToEsServiceImpl implements IKafkaConsumerLogToEsService {

    @Autowired
    private IElasticsearchServiceApi iElasticsearchServiceApi;
    @Autowired
    private IIndexManageServiceApi iIndexManageServiceApi;

    @Override
    public void kafkaConsumerSystemManageLogToEs(Optional message){
        String msg = (String) message.get();
        SendLogToKafkaBaseDto kafkaSendSystemLogBaseDto = JSON.parseObject(msg, SendLogToKafkaBaseDto.class);
        LogCollectorDto esLogCollectorDTO = new LogCollectorDto();
        EsUtils.upIndexName(esLogCollectorDTO.getClass(), kafkaSendSystemLogBaseDto.getServiceName());
        if (iIndexManageServiceApi.createIndexSettingsMappings(esLogCollectorDTO.getClass())) {
            BeanUtils.copyProperties(kafkaSendSystemLogBaseDto, esLogCollectorDTO);
            iElasticsearchServiceApi.addData(esLogCollectorDTO, true);
        }
    }
}
