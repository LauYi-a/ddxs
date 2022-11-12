package com.ddx.util.log.mc.collector;

import com.ddx.util.basis.constant.ConstantUtils;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.utils.ThreadPoolUtils;
import com.ddx.util.log.mc.config.LogmcConfig;
import com.ddx.util.log.mc.model.dto.LogMonitorCollectorDTO;
import com.ddx.util.log.mc.utils.ConfigFileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
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

    public LogCollectorDevice(LogmcConfig logmcConfig){
        this.logmcConfig = logmcConfig;
    }

    public void readLogContentAndSubmitOffset(File file){
        ThreadPoolUtils.execute(ConstantUtils.THREAD_POOL_LOG_MC,() -> {
            FileInputStream inputStream = null;
            BufferedReader bufferedReader = null;
            try {
                LogMonitorCollectorDTO logMonitorCollector = ConfigFileUtil.readConfigJsonFile(logmcConfig.getLogMcName(), LogMonitorCollectorDTO.class);
                Path path = Paths.get(file.getPath());
                long lineSum = Files.lines(path).count();
                if (!Objects.equals(lineSum,logMonitorCollector.getOffset())) {
                    inputStream = new FileInputStream(file.getPath());
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    long skip = logMonitorCollector.getOffset()<lineSum?logMonitorCollector.getOffset():0;
                    Stream<String> stringStream = bufferedReader.lines().skip(skip).limit(lineSum - skip);
                    String data = stringStream.collect(Collectors.joining("\n"))+"\n";
                    System.out.println(data);
               /* LogMonitorFileVo monitorFileVo = LogMonitorFileVo.builder()
                        .data(data)
                        .serviceName(serviceName)
                        .ip(IPUtils.getIp())
                        .build();
                producer.send(monitorFileVo, StringUtil.getUUID(),ConstantUtils.DDX_SYSTEM_MANAGE_TOPIC);*/
                    submitOffset(logmcConfig,lineSum);
                }
            }catch (Exception e){
                ExceptionUtils.businessException(CommonEnumConstant.PromptMessage.LOG_COLLECTOR_ERROR,e.getMessage());
            }finally {
                if (inputStream !=null&&bufferedReader!=null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void submitOffset(LogmcConfig logmcConfig,long offset){
        LogMonitorCollectorDTO logMonitorCollector = ConfigFileUtil.readConfigJsonFile(logmcConfig.getLogMcName(), LogMonitorCollectorDTO.class);
        logMonitorCollector.setOffset(offset);
        ConfigFileUtil.writeConfigJsonFile(logmcConfig.getLogMcName(),logMonitorCollector);
    }

    public void deleteOffset(LogmcConfig logmcConfig,long offset){
        LogMonitorCollectorDTO logMonitorCollector = ConfigFileUtil.readConfigJsonFile(logmcConfig.getLogMcName(), LogMonitorCollectorDTO.class);
        logMonitorCollector.setOffset(offset);
        ConfigFileUtil.writeConfigJsonFile(logmcConfig.getLogMcName(),logMonitorCollector);
    }
}
