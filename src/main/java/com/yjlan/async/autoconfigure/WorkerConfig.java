package com.yjlan.async.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yjlan
 * @version V1.0
 * @Description Worker的config
 * @date 2022.02.23 16:25
 */
@ConfigurationProperties(prefix = "async")
public class WorkerConfig {
    
    private List<Config> workers = new ArrayList<Config>();
    
    public List<Config> getWorkers() {
        return workers;
    }
    
    public void setWorkers(List<Config> workers) {
        this.workers = workers;
    }
    
    public static class  Config {
        
        private String channel;
        
        private Integer ringBufferSize;
        
        private Integer eventHandlerNum;
    
        public String getChannel() {
            return channel;
        }
    
        public void setChannel(String channel) {
            this.channel = channel;
        }
    
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
}
