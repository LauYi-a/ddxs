package com.ddx.log.service.impl;

import com.alibaba.fastjson.JSON;
import com.ddx.log.service.IKafkaConsumerLogToEsService;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.utils.ThreadPoolUtil;
import com.ddx.util.es.dto.EsLogCollectorDTO;
import com.ddx.util.es.result.EsResultData;
import com.ddx.util.es.service.IElasticsearchServiceApi;
import com.ddx.util.log.collector.model.KafkaSendSystemLogBaseDto;
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

    @Override
    public void kafkaConsumerSystemManageLogToEs(Optional message){
        ThreadPoolUtil.execute(BasisConstant.THREAD_POOL_LOG_C_C,()->{
            String msg = (String) message.get();
            KafkaSendSystemLogBaseDto kafkaSendSystemLogBaseDto = JSON.parseObject(msg, KafkaSendSystemLogBaseDto.class);
            String indexName = kafkaSendSystemLogBaseDto.getServiceName()+"-log";
            EsLogCollectorDTO esLogCollectorDTO = new EsLogCollectorDTO();
            BeanUtils.copyProperties(kafkaSendSystemLogBaseDto, esLogCollectorDTO);
            EsResultData esResultData = iElasticsearchServiceApi.addData(esLogCollectorDTO,indexName, true);
            if (!EsResultData.isSuccess(esResultData)) {
                ExceptionUtils.businessException(CommonEnumConstant.PromptMessage.FAILED, esResultData.getMsg());
            }
        });
    }
}
