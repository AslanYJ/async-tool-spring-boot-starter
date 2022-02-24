package com.yjlan.async.handler;

import java.util.List;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.WorkHandler;
import com.yjlan.async.event.BaseEvent;
import com.yjlan.async.event.DispatcherEvent;
import com.yjlan.async.event.WorkerEvent;
import com.yjlan.async.eventbus.WorkerEventBus;
import com.yjlan.async.eventbus.WorkerEventBusManager;
import com.yjlan.async.listener.EventListener;

/**
 * @author yjlan
 * @version V1.0
 * @Description 处理DispatcherEvent的处理器
 * @date 2022.02.24 10:03
 */
public class DispatcherHandler implements WorkHandler<DispatcherEvent> {
    
    
    @Override
    public void onEvent(DispatcherEvent event) throws Exception {
        try {
            dispatchEvent(event);
        }finally {
            event.clear();
        }
    }
    
    
    /**
     * 转发事件
     * @param event 事件
     */
    @SuppressWarnings("rawtypes")
    private void dispatchEvent(DispatcherEvent event) {
        String channel = event.getChannel();
        // 根据channel找到对应的workerEventBus，然后发布
        WorkerEventBusManager workerEventBusManager = WorkerEventBusManager.getInstance();
        final WorkerEventBus workerEventBus = workerEventBusManager.getWorkerEventBus(channel);
        if (workerEventBus == null) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<EventListener> eventListenerList = workerEventBus.getHasAcceptEventListeners(event.getBaseEvent());
        EventTranslator<WorkerEvent> translator = (e,s) -> {
            e.setAsyncContext(event.getAsyncContext());
            e.setBaseEvent(e.getBaseEvent());
            e.setEventListenerList(eventListenerList);
        };
        final boolean publish = workerEventBus.publish(translator);
        if (!publish) {
            throw new RuntimeException(String.format("转发事件失败，channel:%s",channel));
        }
    }
}
