package com.yjlan.async.autoconfigure;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.yjlan.async.annotation.Channel;
import com.yjlan.async.event.DispatcherEvent;
import com.yjlan.async.eventbus.DispatcherEventBus;
import com.yjlan.async.eventbus.WorkerEventBusManager;
import com.yjlan.async.listener.EventListener;
import com.yjlan.async.thread.ExecutorService;
import com.yjlan.async.utils.CglibUtils;

/**
 * @author yjlan
 * @version V1.0
 * @Description 自动装配的类
 * 类似于Redis事件驱动处理模型，一个队列只是负责接收所有的请求，然后基于每个处理请求（Event），根据他们的对应的channel
 * 找到对应的workEventBus，发送到对应的事件处理器，每个workEventBus也对应着自己的一个处理线程池（提高吞吐量）。
 * 每一个处理器找到自己对应的listener处理逻辑。
 * 配置的方式如下：
 * .properties的配置形式如下
 * 	## dispatcher。事件总线，负责分发事件
 * 	async.dispatcher.ringBufferSize=4096
 * 	async.dispatcher.eventHandlerNum=1
 * 	## 对应的 worker的配置
 * 	## channel对应的业务
 * 	async.workers[0].channel=/channel/01/print1
 * 	## distruptor的队列容量
 * 	async.workers[0].ringBufferSize=4096
 * 	## 对应的线程池的线程数量
 * 	async.workers[0].eventHandlerNum=1
 * 	async.workers[1].channel=/channel/02/print2
 * 	async.workers[1].ringBufferSize=4096
 * 	async.workers[1].eventHandlerNum=1
 * 	async.workers[2].channel=/channel/03/print3
 * 	async.workers[2].ringBufferSize=4096
 * 	async.workers[2].eventHandlerNum=1
 *
 *
 *  ## 线程池配置
 *  async.executor.executors[0].threadPool=/channel/01/print1
 * 	async.executor.executors[0].threadCount=1
 * 	async.executor.executors[1].threadPool=/channel/02/print2
 * 	async.executor.executors[1].threadCount=1
 * 	async.executor.executors[2].threadPool=/channel/03/print3
 * 	async.executor.executors[2].threadCount=1
 * @date 2022.02.23 15:53
 */
@Configuration
@EnableConfigurationProperties({DispatcherConfig.class,WorkerConfig.class,ExecutorConfig.class})
public class AsyncToolAutoConfiguration implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    
    private ApplicationContext applicationContext;
    
    private final DispatcherConfig dispatcherConfig;
    
    private final WorkerConfig workerConfig;
    
    private final ExecutorConfig executorConfig;
    
    public AsyncToolAutoConfiguration(DispatcherConfig dispatcherConfig,
            WorkerConfig workerConfig, ExecutorConfig executorConfig) {
        this.dispatcherConfig = dispatcherConfig;
        this.workerConfig = workerConfig;
        this.executorConfig = executorConfig;
    }
    
    
    @Bean
    @Conditional(EventBusCondition.class)
    @ConditionalOnMissingBean
    public DispatcherEventBus dispatcherEventBus() {
        return new DispatcherEventBus(dispatcherConfig,workerConfig);
    }
    
    
    @Bean
    @Conditional(ExecutorCondition.class)
    @ConditionalOnMissingBean
    public ExecutorService executorService(ExecutorConfig executorConfig) {
        return new ExecutorService(executorConfig);
    }
    
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 将监听器绑定到对应的EventBus中
        Map<String, EventListener> eventListenerMap = applicationContext.getBeansOfType(EventListener.class);
        WorkerEventBusManager workerEventBusManager = WorkerEventBusManager.getInstance();
        for (EventListener eventListener : eventListenerMap.values()) {
            Class<?> realClazz = CglibUtils.filterCglibProxyClass(eventListener.getClass());
            Channel channel = realClazz.getAnnotation(Channel.class);
            if (channel != null && !channel.value().isEmpty()) {
                workerEventBusManager.getWorkerEventBus(channel.value()).register(eventListener);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
  
}
