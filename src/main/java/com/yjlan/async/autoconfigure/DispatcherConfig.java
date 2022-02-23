package com.yjlan.async.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yjlan
 * @version V1.0
 * @Description Dispatcher的配置
 * @date 2022.02.23 16:13
 */
@ConfigurationProperties(prefix = "async.dispatcher")
public class DispatcherConfig {
    
    private Integer ringBufferSize;
    
    private Integer eventHandlerNum;
    
    public Integer getRingBufferSize() {
        return ringBufferSize;
    }
    
    public void setRingBufferSize(Integer ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }
    
    public Integer getEventHandlerNum() {
        return eventHandlerNum;
    }
    
    public void setEventHandlerNum(Integer eventHandlerNum) {
        this.eventHandlerNum = eventHandlerNum;
    }
}
