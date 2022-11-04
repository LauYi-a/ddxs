package com.ddx.common.service;

/**
 * @ClassName: FileMonitorService
 * @Description: 文件监听接口
 * @Author: YI.LAU
 * @Date: 2022年10月27日 11:31
 * @Version: 1.0
 */
public interface FileMonitorService {

    /**
     * 启动 日志文件监听
     * @param path 监听路径
     * @param interval 监听频率
     * @throws Exception
     */
    void startLogFileMonitor(String path,long interval);
}
