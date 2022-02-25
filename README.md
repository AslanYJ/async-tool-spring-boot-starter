# 1.async-tool-spring-boot-starter简介
一个处理“发布-订阅”模式的工具。使用Disruptor作为内存队列，采用无锁化充分利用CPU的缓存，性能高。
# 2.处理流程图
![image](https://user-images.githubusercontent.com/20811435/155641696-7e25d816-3665-4779-b7af-43576ed7c073.png)
# 3.使用方式
## 3.1 修改对应你的springboot版本
由于是作为一个spring-starter引入，因此在引入你的工程的时候，要修改对应的SpringBoot版本
![image](https://user-images.githubusercontent.com/20811435/155641828-5b7f4ceb-85f7-40b8-8992-ed647878e0b7.png)

## 3.2 引入依赖
通过maven install到本地，或者deploy私有仓库。

## 3.3 配置信息
在使用的时候需要在.properties中配置以下文件（.yml文件改成对应的语法即可）
```properties
## dispatcher。事件总线，负责分发事件
## ringBufferSize数组的长度
async.dispatcher.ringBufferSize=4096
async.dispatcher.eventHandlerNum=1
## 对应的 worker的配置
## channel对应的业务
async.workers[0].channel=/channel/01/print1
## distruptor的队列容量
async.workers[0].ringBufferSize=4096
## 对应handler的数量
async.workers[0].eventHandlerNum=1
async.workers[1].channel=/channel/02/print2
async.workers[1].ringBufferSize=4096
async.workers[1].eventHandlerNum=1
async.workers[2].channel=/channel/03/print3
async.workers[2].ringBufferSize=4096
async.workers[2].eventHandlerNum=1
```

## 3.4 在项目中使用
### 3.4.1 定义一个内容上下文
```java

/**
 * @author yjlan
 * @version V1.0
 * @Description 要传递的内容
 * @date 2022.02.24 16:30
 */
public class PrintContext extends AsyncContext {
    
    /**
     * 内容
     */
    private String content;
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}

```

### 3.4.2 定义evnet
event只是起到标识的作用，标识是属于哪个业务的，当然你可以定义多个event，然后链式传输，这里给出一个DEOM
```java
/**
 * @author yjlan
 * @version V1.0
 * @Description (这里用一句话描述这个类的作用)
 * @date 2022.02.24 16:26
 */
public class Print1Event extends BaseEvent {

}

```

### 3.4.3 定义listener
listener是我们具体处理业务的类，一个event对应一个listener。这里就可以写对应的业务逻辑了，如果是有步骤的业务逻辑，那么当前业务处理完后，调用
dispatcherEventBus.publishEvent(PrintEventHolder.PRINT_2_EVENT,ChannelKey.PRINT2_CHANNEL,printContext);
发布到队列中，然后分发都对应的workerEevnetBus即可。
```java

@Component
@Channel(ChannelKey.PRINT1_CHANNEL)
public class Print1Listener implements EventListener<Print1Event> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Print1Listener.class);
    
    @Resource
    private DispatcherEventBus dispatcherEventBus;
    
    @Override
    public boolean accept(BaseEvent baseEvent) {
        return baseEvent instanceof Print1Event;
    }
    
    @Override
    public void execute(Print1Event print1Event, AsyncContext asyncContext) {
        PrintContext printContext = (PrintContext) asyncContext;
        LOGGER.info("信息--------------------- msg:{}",printContext.getContent());
        printContext.setContent("Print1打印完毕，Print2打印");
        // 发布到下一个步骤
        dispatcherEventBus.publishEvent(PrintEventHolder.PRINT_2_EVENT,ChannelKey.PRINT2_CHANNEL,printContext);
    }
}

```
### 3.4.5 其他定义
一些和流程无关的定义，比如对应的channelKey可以用一个接口来定义他们的常量，对应的事件类型也可以用一个常量初始化
**常量初始化**
```java
/**
 * @author yjlan
 * @version V1.0
 * @Description 初始化所有数据类型
 * @date 2022.02.24 16:44
 */
public class PrintEventHolder {
    
    public static final Print1Event PRINT_1_EVENT = new Print1Event();
    
    public static final Print2Event PRINT_2_EVENT = new Print2Event();
    
    public static final Print3Event PRINT_3_EVENT = new Print3Event();
    
    
}

```
**key的常量**
```
/**
 * @author yjlan
 * @version V1.0
 * @Description channelKey的常量部分
 * @date 2022.02.24 16:29
 */
public interface ChannelKey {
    
    String PRINT1_CHANNEL = "/channel/01/print1";
    
    String PRINT2_CHANNEL = "/channel/02/print2";
    
    String PRINT3_CHANNEL = "/channel/03/print3";
    
}

```
### 3.4.6 使用
```java
  @Resource
    private DispatcherEventBus dispatcherEventBus;
    @GetMapping("test")
    public void test() {
        PrintContext printContext = new PrintContext();
        printContext.setContent("test");
        dispatcherEventBus.publishEvent(PrintEventHolder.PRINT_1_EVENT, ChannelKey.PRINT1_CHANNEL,printContext);
    }
```
