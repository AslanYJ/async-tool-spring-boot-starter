package com.yjlan.async.autoconfigure;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author yjlan
 * @version V1.0
 * @Description DispatcherEventBus的ExecutorCondition。用来判断是否注入ExecutorService
 * @date 2022.02.23 16:25
 */
public class ExecutorCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().containsProperty("async.executor.executors[0].threadPool")
                && context.getEnvironment().containsProperty("async.executor.executors[0].threadCount");
    }

}