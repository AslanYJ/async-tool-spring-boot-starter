package com.yjlan.async.eventbus;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import com.yjlan.async.AsyncContext;
import com.yjlan.async.autoconfigure.DispatcherConfig;
import com.yjlan.async.autoconfigure.WorkerConfig;
import com.yjlan.async.autoconfigure.WorkerConfig.Config;
import com.yjlan.async.event.BaseEvent;
import com.yjlan.async.event.DispatcherEvent;
import com.yjlan.async.handler.DispatcherHandler;
import com.yjlan.async.thread.CustomizeThreadFactory;

/**
 * @author yjlan
 * @version V1.0
 * @Description 负责存储DispatcherEvent队列的处理
 * @date 2022.02.24 09:31
 */
public class DispatcherEventBus {
    
    
    private final Disruptor<DispatcherEvent> dispatcherRingBuffer;
    
    
    public DispatcherEventBus(DispatcherConfig dispatcherConfig, WorkerConfig workerConfig) {
        // 初始化workerEventBus
        WorkerEventBusManager workerEventBusManager = WorkerEventBusManager.getInstance();
        workerEventBusManager.initWorkerEventBus(workerConfig);
        // 初始化dispatcherEventBus
        dispatcherRingBuffer = new Disruptor<>(DispatcherEvent::new, dispatcherConfig.getRingBufferSize(),
                CustomizeThreadFactory.getInstance("dispatcherEventBus"));
    
        DispatcherHandler[] dispatcherHandlers = new DispatcherHandler[dispatcherConfig.getEventHandlerNum()];
        //noinspection ConstantConditions
        for (int i = 0; i < dispatcherHandlers.length; i++) {
            dispatcherHandlers[i] = new DispatcherHandler();
        }
        // 设置handler
        dispatcherRingBuffer.handleEventsWithWorkerPool(dispatcherHandlers);
        dispatcherRingBuffer.start();
    }
    
   
    
    
    /**
     * 发布事件
     * @param baseEvent 对应的基础事件
     * @param channel 渠道
     * @param asyncContext 内容
     */
    public boolean publishEvent(BaseEvent baseEvent,String channel, AsyncContext asyncContext) {
        // 将对应的数据数据封装成事件
        EventTranslator<DispatcherEvent> translator = (e,s) -> {
            e.setChannel(channel);
            e.setAsyncContext(asyncContext);
            e.setBaseEvent(baseEvent);
        };
        return dispatcherRingBuffer.getRingBuffer().tryPublishEvent(translator);
    }
    
}
