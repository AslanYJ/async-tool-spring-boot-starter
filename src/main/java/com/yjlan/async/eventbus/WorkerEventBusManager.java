package com.yjlan.async.eventbus;

import java.util.concurrent.ConcurrentHashMap;

import com.yjlan.async.autoconfigure.WorkerConfig;
import com.yjlan.async.autoconfigure.WorkerConfig.Config;
import com.yjlan.async.event.WorkerEvent;
import com.yjlan.async.handler.WorkerHandler;

/**
 * @author yjlan
 * @version V1.0
 * @Description 负责管理所有WorkerEventBus
 * @date 2022.02.24 10:39
 */
public class WorkerEventBusManager {
    
    /**
     * 存储内存中所有的EventBus
     * key：channel
     * value：WorkerEventBus
     */
    @SuppressWarnings("rawtypes")
    private final ConcurrentHashMap<String,WorkerEventBus> WORKER_BUS_MAP = new ConcurrentHashMap<>();
    
    private WorkerEventBusManager() {
    
    }
    
    private static class WorkerManagerHolder {
        private static final WorkerEventBusManager INSTANCE = new WorkerEventBusManager();
    }
    
    public static WorkerEventBusManager getInstance() {
        return WorkerManagerHolder.INSTANCE;
    }
    
    /**
     * 初始化WorkerEventBus
     * @param workerConfig 配置
     */
    public void initWorkerEventBus(WorkerConfig workerConfig) {
        for (Config config : workerConfig.getWorkers()) {
            WORKER_BUS_MAP.computeIfAbsent(config.getChannel(), k -> new WorkerEventBus<>(
                    config.getRingBufferSize(),
                    config.getEventHandlerNum(),
                    WorkerEvent::new,
                    WorkerHandler::new
            ));
        }
    }
    
    
    @SuppressWarnings("rawtypes")
    public WorkerEventBus getWorkerEventBus(String channel) {
        return WORKER_BUS_MAP.get(channel);
    }
}
