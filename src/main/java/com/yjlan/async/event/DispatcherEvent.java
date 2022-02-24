package com.yjlan.async.event;

import com.yjlan.async.AsyncContext;

/**
 * @author yjlan
 * @version V1.0
 * @Description 转发事件的Event
 * @date 2022.02.24 09:26
 */
public class DispatcherEvent {

    private String channel;
    
    private BaseEvent baseEvent;
    
    private AsyncContext asyncContext;
    
    
    public void clear() {
        this.channel = null;
        this.baseEvent = null;
        this.asyncContext = null;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public void setChannel(String channel) {
        this.channel = channel;
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
}
