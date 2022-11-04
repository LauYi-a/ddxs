package com.ddx.common.service.impl;

import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.model.vo.SysLogFileMonitorVo;
import com.ddx.basis.utils.IPUtils;
import com.ddx.basis.utils.StringUtil;
import com.ddx.common.mq.IProducer;
import com.ddx.common.utils.ApplicationContextUtil;
import com.ddx.common.utils.RedisTemplateUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName: ReadLogUtils
 * @Description: 日志采集
 * @Author: YI.LAU
 * @Date: 2022年10月28日 14:07
 * @Version: 1.0
 */
public class LogReadServiceImpl {

    private String serviceName;
    private static RedisTemplateUtils redisTemplate;
    private static IProducer producer;
    static{
        redisTemplate = ApplicationContextUtil.getBean(RedisTemplateUtils.class);
        producer = ApplicationContextUtil.getBean(IProducer.class);
    }

    public LogReadServiceImpl(String name){
        serviceName = name;
    }

    public void readLogContentAndSubmitOffset(File file)throws Exception{
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            if (!redisTemplate.hasKey(ConstantUtils.SYSTEM_LOG_OFFSET+serviceName+":"+ IPUtils.getIp())){
                submitOffset(0);
            }
            Path path = Paths.get(file.getPath());
            long lineSum = Files.lines(path).count();
            long skipCount = Long.valueOf(String.valueOf(redisTemplate.get(ConstantUtils.SYSTEM_LOG_OFFSET+serviceName+":"+IPUtils.getIp())));
            if (!Objects.equals(lineSum,skipCount)) {
                inputStream = new FileInputStream(file.getPath());
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                long skip = skipCount<lineSum?skipCount:0;
                Stream<String> stringStream = bufferedReader.lines().skip(skip).limit(lineSum - skip);
                String data = stringStream.collect(Collectors.joining("\n"))+"\n";
                SysLogFileMonitorVo logFileMonitorVo = SysLogFileMonitorVo.builder()
                        .data(data)
                        .serviceName(serviceName)
                        .ip(IPUtils.getIp())
                        .build();
                producer.send(logFileMonitorVo, StringUtil.getUUID(),ConstantUtils.DDX_SYSTEM_MANAGE_TOPIC);
                submitOffset(lineSum);
            }
        }finally {
            if (inputStream !=null&&bufferedReader!=null) {
                inputStream.close();
                bufferedReader.close();
            }
        }
    }

    public void submitOffset(long offset){
        redisTemplate.set(ConstantUtils.SYSTEM_LOG_OFFSET+serviceName+":"+IPUtils.getIp(),offset);
    }

    public void deleteOffset(){
        redisTemplate.del(ConstantUtils.SYSTEM_LOG_OFFSET+serviceName+":"+IPUtils.getIp());
    }
}
