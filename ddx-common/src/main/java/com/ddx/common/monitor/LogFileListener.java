package com.ddx.common.monitor;

import com.ddx.basis.constant.ConstantUtils;
import com.ddx.common.service.impl.LogReadServiceImpl;
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
public class LogFileListener extends FileAlterationListenerAdaptor {

	private String serviceName;

	public LogFileListener(String name){
		serviceName = name;
	}
	@Override
	public void onStart(FileAlterationObserver observer) {
		super.onStart(observer);
	}

	@Override
	public void onFileCreate(File file) {
		if (Objects.equals(file.getName(), ConstantUtils.FILE_NAME)){
			if (file.canRead()) {
				LogReadServiceImpl readLogService = new LogReadServiceImpl(serviceName);
				readLogService.submitOffset(0);
				System.out.println(file.canRead());
			}
		}
	}

	@Override
	public void onFileChange(File file) {
		if (Objects.equals(file.getName(),ConstantUtils.FILE_NAME)){
			try {
				LogReadServiceImpl readLogService = new LogReadServiceImpl(serviceName);
				readLogService.readLogContentAndSubmitOffset(file);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFileDelete(File file) {
		if (Objects.equals(file.getName(),ConstantUtils.FILE_NAME)){
			LogReadServiceImpl readLogService = new LogReadServiceImpl(serviceName);
			readLogService.deleteOffset();
		}
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		super.onStop(observer);
	}
}