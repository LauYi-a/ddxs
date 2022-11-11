package com.ddx.log.mc.monitor;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;


/**
 * @ClassName: FileMonitorUnit
 * @Description: 文件监控装置
 * @Author: YI.LAU
 * @Date: 2022年10月27日 11:30
 * @Version: 1.0
 */
public class FileMonitorUnit {

	private FileAlterationMonitor monitor;

	public FileMonitorUnit(long interval) {
		monitor = new FileAlterationMonitor(interval);
	}

	/**
	 * 给文件添加监听
	 *
	 * @param path     文件路径
	 * @param listener 文件监听器
	 */
	public void monitor(String path, FileAlterationListener listener) {
		FileAlterationObserver observer = new FileAlterationObserver(new File(path));
		monitor.addObserver(observer);
		observer.addListener(listener);
	}

	/**
	 * 停止监听
	 * @throws Exception
	 */
	public void stop() throws Exception {
		monitor.stop();
	}

	/**
	 * 启动监听
	 * @throws Exception
	 */
	public void start() throws Exception {
		monitor.start();

	}
}