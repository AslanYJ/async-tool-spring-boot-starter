package com.yjlan.async.event;

import java.util.List;

import com.yjlan.async.AsyncContext;
import com.yjlan.async.listener.EventListener;

/**
 * @author yjlan
 * @version V1.0
 * @Description WorkerEvent。一个WorkerEvent包括以下信息
 * 1. 具体的事件类型
 * 2. 上下文（事件对应的数据）
 * 3.
 * @date 2022.02.24 09:12
 */
public class WorkerEvent {
    
    /**
     * 具体的事件类型
     */
    private BaseEvent baseEvent;
    
    /**
     * 事件的上下文
     */
    private AsyncContext asyncContext;
    
    
    
    @SuppressWarnings("rawtypes")
    private List<EventListener> eventListenerList;
    
    
    public void clear() {
        this.baseEvent = null;
        this.asyncContext = null;
        this.eventListenerList = null;
    }
    
    public BaseEvent getBaseEvent() {
        return baseEvent;
    }
    
    public void setBaseEvent(BaseEvent baseEvent) {
        this.baseEvent = baseEvent;
    }
    
    public AsyncContext getAsyncContext() {
        return asyncContext;
    }
    
    public void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }
    
    public List<EventListener> getEventListenerList() {
        return eventListenerList;
    }
    
    public void setEventListenerList(List<EventListener> eventListenerList) {
        this.eventListenerList = eventListenerList;
    }
}
