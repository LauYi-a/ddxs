package com.ddx.log.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.basis.model.vo.SysLogFileMonitorVo;
import com.ddx.common.config.SftpConfig;
import com.ddx.common.utils.FileUtil;
import com.ddx.log.service.IKafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

/**
 * @ClassName: KafkaServiceImpl
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年11月04日 10:53
 * @Version: 1.0
 */
@Service
public class KafkaServiceImpl implements IKafkaService {

    @Value("${service-log.path:}")
    private String serviceLogPath;
    @Autowired
    private SftpConfig sftpConfig;

    @Override
    public void writerLogFile(Acknowledgment ack, Optional message) {
        if (message.isPresent()) {
            try {
                SysLogFileMonitorVo logFileMonitorVo = JSONObject.parseObject(message.get().toString(),SysLogFileMonitorVo.class);
                String path = serviceLogPath + File.separator +logFileMonitorVo.getServiceName()+ File.separator + logFileMonitorVo.getIp();
                FileUtil.connectSftpWriterFile(sftpConfig,path, ConstantUtils.FILE_NAME,logFileMonitorVo.getData());
                ack.acknowledge();
            } catch (Exception e) {
                ExceptionUtils.businessException(true, CommonEnumConstant.PromptMessage.KAFKA_CONSUMER_ERROR,String.format(CommonEnumConstant.PromptMessage.KAFKA_CONSUMER_ERROR.getMsg(),e.getMessage()));
                e.printStackTrace();
            }
        }
    }
}
