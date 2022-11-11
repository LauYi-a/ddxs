package com.ddx.log.mc.collector;

/**
 * @ClassName: LogCollectorDevice
 * @Description: 日志采集装置
 * @Author: YI.LAU
 * @Date: 2022年10月28日 14:07
 * @Version: 1.0
 */
public class LogCollectorDevice {

    /*private String serviceName;

    public LogCollectorDevice(String name){
        serviceName = name;
    }

   *//* public void readLogContentAndSubmitOffset(File file)throws Exception{
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
               *//**//* LogMonitorFileVo monitorFileVo = LogMonitorFileVo.builder()
                        .data(data)
                        .serviceName(serviceName)
                        .ip(IPUtils.getIp())
                        .build();
                producer.send(monitorFileVo, StringUtil.getUUID(),ConstantUtils.DDX_SYSTEM_MANAGE_TOPIC);*//**//*
                submitOffset(lineSum);
            }
        }finally {
            if (inputStream !=null&&bufferedReader!=null) {
                inputStream.close();
                bufferedReader.close();
            }
        }
    }*//*

    public void submitOffset(long offset){
        redisTemplate.set(ConstantUtils.SYSTEM_LOG_OFFSET+serviceName+":"+IPUtils.getIp(),offset);
    }

    public void deleteOffset(){
        redisTemplate.del(ConstantUtils.SYSTEM_LOG_OFFSET+serviceName+":"+IPUtils.getIp());
    }*/
}
