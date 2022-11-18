package com.ddx.util.log.mc.collector;

import com.ddx.util.basis.constant.BasisConstantConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.utils.ApplicationContextUtil;
import com.ddx.util.basis.utils.ThreadPoolUtils;
import com.ddx.util.kafka.api.IProducerApi;
import com.ddx.util.kafka.dto.LogCollectorEsDTO;
import com.ddx.util.log.mc.config.LogmcConfig;
import com.ddx.util.log.mc.model.LogMonitorCollectorConfigDTO;
import com.ddx.util.log.mc.utils.ConfigFileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName: LogCollectorDevice
 * @Description: 日志采集装置
 * @Author: YI.LAU
 * @Date: 2022年10月28日 14:07
 * @Version: 1.0
 */
@Slf4j
public class LogCollectorDevice {

    private LogmcConfig logmcConfig;
    private static IProducerApi iProducerApi;
    static {
        iProducerApi = ApplicationContextUtil.getBean(IProducerApi.class);
    }
    public LogCollectorDevice(LogmcConfig logmcConfig){
        this.logmcConfig = logmcConfig;
    }

    public void readLogContentAndSubmitOffset(File file){
        ThreadPoolUtils.execute(BasisConstantConstant.THREAD_POOL_LOG_MC,() -> {
            FileInputStream inputStream = null;
            BufferedReader bufferedReader = null;
            try {
                LogMonitorCollectorConfigDTO logMonitorCollector = ConfigFileUtil.readConfigJsonFile(logmcConfig.getLogMcName(), LogMonitorCollectorConfigDTO.class);
                Path path = Paths.get(file.getPath());
                long lineSum = Files.lines(path).count();
                if (!Objects.equals(lineSum,logMonitorCollector.getOffset())) {
                    inputStream = new FileInputStream(file.getPath());
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    long skipLine = logMonitorCollector.getOffset()<lineSum?logMonitorCollector.getOffset():0;
                    long sumReadLine = lineSum - skipLine;
                    Stream<String> stringStream = bufferedReader.lines().skip(skipLine).limit(sumReadLine);
                    String data = stringStream.collect(Collectors.joining("\n")) + "\n";
                    String topic = logmcConfig.getServiceName()+"-log";
                    boolean isOk = iProducerApi.send(LogCollectorEsDTO.builder()
                            .docId(UUID.randomUUID().toString())
                            .ip(logmcConfig.getIpAddress())
                            .serviceName(logmcConfig.getServiceName())
                            .text(data)
                            .build(),UUID.randomUUID().toString(),topic);
                    if (isOk){
                        submitOffset(logmcConfig, lineSum);
                    }
                }
            }catch (Exception e){
                ExceptionUtils.businessException(CommonEnumConstant.PromptMessage.LOG_COLLECTOR_ERROR,e.getMessage());
            }finally {
                if (inputStream !=null&&bufferedReader!=null) {
                    try {
                        inputStream.close();
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void submitOffset(LogmcConfig logmcConfig,long offset){
        LogMonitorCollectorConfigDTO logMonitorCollector = ConfigFileUtil.readConfigJsonFile(logmcConfig.getLogMcName(), LogMonitorCollectorConfigDTO.class);
        logMonitorCollector.setOffset(offset);
        ConfigFileUtil.writeConfigJsonFile(logmcConfig.getLogMcName(),logMonitorCollector);
    }

    public void deleteOffset(LogmcConfig logmcConfig,long offset){
        LogMonitorCollectorConfigDTO logMonitorCollector = ConfigFileUtil.readConfigJsonFile(logmcConfig.getLogMcName(), LogMonitorCollectorConfigDTO.class);
        logMonitorCollector.setOffset(offset);
        ConfigFileUtil.writeConfigJsonFile(logmcConfig.getLogMcName(),logMonitorCollector);
    }
}
