package com.yjlan.async.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yjlan
 * @version V1.0
 * @Description 线程池的配置
 * @date 2022.02.23 16:36
 */
@ConfigurationProperties(prefix = "async.executor")
public class ExecutorConfig {
    
    private List<Config> executors = new ArrayList<>();
    
    public List<Config> getExecutors() {
        return executors;
    }
    
    public void setExecutors(List<Config> executors) {
        this.executors = executors;
    }
    
    
    public static class Config {
        private String threadPool;
        private Integer threadCount;
        
        public String getThreadPool() {
            return threadPool;
        }
        
        public void setThreadPool(String threadPool) {
            this.threadPool = threadPool;
        }
        
        public Integer getThreadCount() {
            return threadCount;
        }
        
        public void setThreadCount(Integer threadCount) {
            this.threadCount = threadCount;
        }
    }
}
