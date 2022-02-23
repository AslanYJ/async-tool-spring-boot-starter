package com.yjlan.async.autoconfigure;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author yjlan
 * @version V1.0
 * @Description 针对DispatcherEventBus的EventBusCondition的Config，用来判断是否注入dispatcherEventBus
 * @date 2022.02.23 16:25
 */
public class EventBusCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().containsProperty("async.dispatcher.ringBufferSize")
                && context.getEnvironment().containsProperty("async.dispatcher.eventHandlerNum")
                && context.getEnvironment().containsProperty("async.workers[0].channel")
                && context.getEnvironment().containsProperty("async.workers[0].ringBufferSize")
                && context.getEnvironment().containsProperty("async.workers[0].eventHandlerNum");
    }

}
