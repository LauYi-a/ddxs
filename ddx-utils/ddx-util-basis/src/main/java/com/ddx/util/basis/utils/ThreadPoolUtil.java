package com.ddx.util.basis.utils;

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

    /**
     * 超过最长队列会丢弃任务
     * @param poolName
     * @return
     */
    private static synchronized ThreadPoolExecutor getThreadPool(String poolName) {
        if (threadPool == null) {
            // 获取处理器数量
            int cpuNum = Runtime.getRuntime().availableProcessors();
            // 根据cpu数量,计算出合理的线程并发数
            int maxPoolSize = cpuNum * 2 + 1;
            threadPool = new ThreadPoolExecutor(maxPoolSize - 1,//核心线程数
                maxPoolSize,//最大线程数
                KEEP_ALIVE_TIME,//闲置线程存活时间
                TimeUnit.MILLISECONDS,//时间单位
                new LinkedBlockingDeque<>(BLOCKING_QUEUE_LENGTH),//线程队列
                new ThreadFactoryBuilder().setNameFormat(poolName +"-%d").build(),//线程工厂
                new ThreadPoolExecutor.AbortPolicy() {
                    //当前线程数已经超过最大线程数时的异常处理策略
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                        log.warn("线程数量过多:当前运行线程总数[{}]活动线程数[{}]等待队列已满,等待运行任务数[{}]",
                                e.getPoolSize(),
                                e.getActiveCount(),
                                e.getQueue().size());
                    }
                });
        }
        return threadPool;
    }

    public static void main(String[] args) {
        int loop = 2000;
        for (int i = 0; i < loop; i++) {
            ThreadPoolUtil.execute("干爆他",() -> {
                log.info("干活好累");
                try {
                    Thread.sleep(10);
                    log.info("终于干完了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        log.info("不用等他，我们先干");
    }
}