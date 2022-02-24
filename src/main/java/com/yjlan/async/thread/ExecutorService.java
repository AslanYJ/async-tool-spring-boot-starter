package com.yjlan.async.thread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.yjlan.async.autoconfigure.ExecutorConfig;
import com.yjlan.async.autoconfigure.ExecutorConfig.Config;

/**
 * @author yjlan
 * @version V1.0
 * @Description 封装每一个channel对应的线程池
 * @date 2022.02.24 14:14
 */
public class ExecutorService {
    
    private final ConcurrentHashMap<String,ExecuteThreadPool> POOL = new ConcurrentHashMap<>();
    
    
    public ExecutorService(ExecutorConfig executorConfig) {
        for (Config config : executorConfig.getExecutors()) {
            POOL.putIfAbsent(config.getThreadPool(),
                    new ExecuteThreadPool(config.getThreadPool(),config.getThreadCount()));
        }
    }
    
    
    public void execute(String channel,Runnable task) {
        final ExecuteThreadPool executeThreadPool = POOL.get(channel);
        if (executeThreadPool != null) {
            executeThreadPool.execute(task);
        }
    }
    
    
    
    public static class ExecuteThreadPool {
        
        private final Semaphore semaphore;
        
        
        private final ThreadPoolExecutor threadPoolExecutor;
        
        
        public ExecuteThreadPool(String name, int permits) {
            semaphore = new Semaphore(permits);
            
            threadPoolExecutor = new ThreadPoolExecutor(0,
                    permits * 2,
                    60,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<>(),
                    CustomizeThreadFactory.getInstance(name));
            
        }
        
        
        public void execute(Runnable task) {
            semaphore.acquireUninterruptibly();
            // 执行对应的任务逻辑
            threadPoolExecutor.submit(() -> {
                try {
                    task.run();
                } finally {
                    semaphore.release();
                }
            });
        }
    }
    
}
