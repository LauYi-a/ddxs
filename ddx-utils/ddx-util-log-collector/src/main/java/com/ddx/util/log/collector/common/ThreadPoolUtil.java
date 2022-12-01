package com.ddx.util.log.collector.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;


/**
 * @ClassName: ThreadPoolUtil
 * @Description: 通用线程工具类，创建线程池后不需要关闭
 * @Author: YI.LAU
 * @Date: 2022年05月11日
 * @Version: 1.0
 */
@Slf4j
public class ThreadPoolUtil {

    private static ThreadPoolExecutor threadPool= null;
    // 等待队列长度
    private static final int BLOCKING_QUEUE_LENGTH= 1000;
    // 闲置线程存活时间
    private static final int KEEP_ALIVE_TIME= 60 * 1000;

    private ThreadPoolUtil() {
        throw new IllegalStateException("utility class");
    }


    /**
     * 无返回值直接执行
     * 默认计算出合理的线程并发数
     * 线程存活时间 60 * 1000
     * 等待队列长度 1000 个
     * @param poolName 线程池名称
     * @param runnable 需要运行的任务
     */
    public static void execute(String poolName,Runnable runnable){
        getThreadPool(poolName).execute(runnable);
    }

    /**
     * 有返回值执行
     * 默认计算出合理的线程并发数
     * 线程存活时间 60 * 1000
     * 最长等待队列 1000
     * 主线程中使用Future.get()获取返回值时会阻塞主线程,直到任务执行完毕
     * Future.get(3, TimeUnit.SECONDS) 可设置线程执行超时时间
     * @param callable 需要运行的任务
     * @param poolName 线程池名称
     */
    public static <T> Future<T> submit(String poolName,Callable<T> callable) {
        return getThreadPool(poolName).submit(callable);
    }

    private static synchronized ThreadPoolExecutor getThreadPool(String poolName) {
        if (threadPool == null) {
            // 获取处理器数量
            int cpuNum = Runtime.getRuntime().availableProcessors();
            // 根据cpu数量,计算出合理的线程并发数
            int maxPoolSize = cpuNum * 2 + 1;
            // 核心线程数、最大线程数、闲置线程存活时间、时间单位、线程队列、线程工厂、当前线程数已经超过最大线程数时的异常处理策略
            threadPool = new ThreadPoolExecutor(maxPoolSize - 1,
                maxPoolSize,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(BLOCKING_QUEUE_LENGTH),
                new ThreadFactoryBuilder().setNameFormat(poolName +"-%d").build(),
                new ThreadPoolExecutor.AbortPolicy() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                        log.warn("线程数量过多\n当前运行线程总数：{}\n活动线程数：{}\n等待队列已满,等待运行任务数:{}",
                            e.getPoolSize(),
                            e.getActiveCount(),
                            e.getQueue().size());
                    }
                });
        }
        return threadPool;
    }
}