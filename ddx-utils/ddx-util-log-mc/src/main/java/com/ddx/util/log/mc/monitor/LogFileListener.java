package com.ddx.util.log.mc.monitor;

import com.ddx.util.basis.utils.ApplicationContextUtil;
import com.ddx.util.log.mc.collector.LogCollectorDevice;
import com.ddx.util.log.mc.config.LogmcConfig;
import com.ddx.util.log.mc.model.dto.LogMonitorCollectorConfigDTO;
import com.ddx.util.log.mc.utils.ConfigFileUtil;
import com.ddx.util.redis.constant.LockConstant;
import com.ddx.util.redis.redission.RedisLock;
import com.ddx.util.redis.result.LockResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.Objects;

/**
 * @ClassName: LogFileListener
 * @Description: 日志文件监听器
 * @Author: YI.LAU
 * @Date: 2022年10月27日 11:30
 * @Version: 1.0
 */
@Slf4j
public class LogFileListener extends FileAlterationListenerAdaptor {

	private LogmcConfig logmcConfig;
	private static RedisLock redisLock;
	static {
		redisLock = ApplicationContextUtil.getBean(RedisLock.class);
	}

	public LogFileListener(LogmcConfig logmcConfig){
		this.logmcConfig = logmcConfig;
	}

	@Override
	public void onStart(FileAlterationObserver observer) {
		super.onStart(observer);
	}

	@Override
	public void onFileCreate(File file) {
		if (Objects.equals(file.getName(), logmcConfig.getMonitorFileName())){
			if (file.canRead()) {
				LogCollectorDevice logCollectorDevice = new LogCollectorDevice(logmcConfig);
				logCollectorDevice.submitOffset(logmcConfig,0);
				System.out.println(file.canRead());
			}
		}
	}

	@Override
	public void onFileChange(File file) {
		if (Objects.equals(file.getName(),logmcConfig.getMonitorFileName())){
			try {
				LogMonitorCollectorConfigDTO logMonitorCollector = ConfigFileUtil.readConfigJsonFile(logmcConfig.getLogMcName(), LogMonitorCollectorConfigDTO.class);
				String lockName = logmcConfig.getServiceName()+":"+logmcConfig.getIpAddress()+":"+LockConstant.LOG_OFFSET_LOCK + logMonitorCollector.getOffset();
				LockResultData resultLockInfo = redisLock.getLock(lockName,()->{
					LogCollectorDevice logCollectorDevice = new LogCollectorDevice(logmcConfig);
					logCollectorDevice.readLogContentAndSubmitOffset(file);
				});
				if (!resultLockInfo.isOk()){
					log.error(resultLockInfo.getMsg());
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFileDelete(File file) {
		if (Objects.equals(file.getName(),logmcConfig.getMonitorFileName())){
			LogCollectorDevice logCollectorDevice = new LogCollectorDevice(logmcConfig);
			logCollectorDevice.deleteOffset(logmcConfig,0);
		}
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		super.onStop(observer);
	}
}