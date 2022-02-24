package com.yjlan.async.thread;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yjlan
 * @version V1.0
 * @Description 自定义的线程工厂
 * @date 2022.02.24 09:56
 */
public class CustomizeThreadFactory implements ThreadFactory {
    
    private static final ConcurrentHashMap<String, CustomizeThreadFactory> BUFFER = new ConcurrentHashMap<>();
    
    
    private final AtomicInteger counter = new AtomicInteger(0);

    private final String name;
    
    private CustomizeThreadFactory(String name) {
        this.name = name;
    }
    
    
    public static CustomizeThreadFactory getInstance(String name) {
        Objects.requireNonNull(name, "必须要传一个线程名字的前缀");
        return BUFFER.computeIfAbsent(name, CustomizeThreadFactory::new);
    }
    
    
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, name + "-" + counter.incrementAndGet());
        thread.setDaemon(true);
        return thread;
    }
}
