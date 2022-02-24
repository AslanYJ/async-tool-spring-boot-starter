package com.yjlan.async.handler;

import com.lmax.disruptor.WorkHandler;
import com.yjlan.async.event.WorkerEvent;
import com.yjlan.async.listener.EventListener;

/**
 * @author yjlan
 * @version V1.0
 * @Description WorkerEventBus的handler
 * @date 2022.02.24 11:08
 */
public class WorkerHandler implements WorkHandler<WorkerEvent> {
    
    
    
    @Override
    public void onEvent(WorkerEvent workerEvent) throws Exception {
        try {
            process(workerEvent);
        }finally {
            workerEvent.clear();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void process(WorkerEvent workerEvent) {
        for (EventListener eventListener : workerEvent.getEventListenerList()) {
            eventListener.execute(workerEvent.getBaseEvent(),workerEvent.getAsyncContext());
        }
    }
    
    
}
