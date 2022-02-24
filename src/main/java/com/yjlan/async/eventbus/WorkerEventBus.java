package com.yjlan.async.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.yjlan.async.event.BaseEvent;
import com.yjlan.async.event.WorkerEvent;
import com.yjlan.async.listener.EventListener;
import com.yjlan.async.thread.CustomizeThreadFactory;

/**
 * @author yjlan
 * @version V1.0
 * @Description WorkerEventBus的队列
 * @date 2022.02.24 10:37
 */
public class WorkerEventBus<E> {
    
    private final Disruptor<E> workerRingBuffer;
    
    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    
    
    @SuppressWarnings("rawtypes")
    private final List<EventListener> eventListenerList = new ArrayList<>();
    
    public WorkerEventBus(Integer ringBufferSize, Integer workerEventBusHandlerNum, EventFactory<E> eEventFactory,
            Supplier<WorkHandler<E>> workHandlerSupplier) {
        
        workerRingBuffer = new Disruptor<>(eEventFactory, ringBufferSize,
                CustomizeThreadFactory.getInstance("workerEventBus"));
        @SuppressWarnings("rawtypes")
        WorkHandler[] workHandlers = new WorkHandler[workerEventBusHandlerNum];
        //noinspection ConstantConditions
        for (int i = 0; i < workHandlers.length; i++) {
            workHandlers[i] = workHandlerSupplier.get();
        }
    
        //noinspection unchecked
        workerRingBuffer.handleEventsWithWorkerPool(workHandlers);
        
        workerRingBuffer.start();
    }
    
    
    /**
     * 注册监听器
     *
     * @param eventListener 监听器
     * @return 注册结果
     */
    @SuppressWarnings("rawtypes")
    public boolean register(EventListener eventListener) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (eventListenerList.contains(eventListener)) {
                return false;
            }
            eventListenerList.add(eventListener);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return true;
    }
    
    /**
     * 获取已经注册注册过的listener
     * @param baseEvent 事件
     * @return 已经注册过的listener
     */
    @SuppressWarnings("rawtypes")
    public List<EventListener> getHasAcceptEventListeners(BaseEvent baseEvent) {
        reentrantReadWriteLock.readLock().lock();
        try {
            return eventListenerList.stream().filter(e -> e.accept(baseEvent))
                    .collect(Collectors.toList());
        }finally {
            reentrantReadWriteLock.readLock().unlock();
        }
    }
    
    /**
     * 发布事件
     * @param translator 事件
     * @return 事件的发布结果
     */
    public boolean publish(EventTranslator<E> translator) {
        return workerRingBuffer.getRingBuffer().tryPublishEvent(translator);
    }
}
