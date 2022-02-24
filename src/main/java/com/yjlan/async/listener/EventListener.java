package com.yjlan.async.listener;

import com.yjlan.async.AsyncContext;
import com.yjlan.async.event.BaseEvent;

/**
 * @author yjlan
 * @version V1.0
 * @Description 针对对应的Event的监听器（发布-订阅模式）
 * @date 2022.02.24 09:21
 */
public interface EventListener<E extends BaseEvent> extends java.util.EventListener {
    
    
    /**
     * 接收这个事件
     * @param baseEvent 自己定义的事件
     * @return 是否接收
     */
    boolean accept(BaseEvent baseEvent);
    
    /**
     * 执行对应的逻辑
     * @param event 事件
     * @param asyncContext 上线文
     */
    void execute(E event, AsyncContext asyncContext);
}
