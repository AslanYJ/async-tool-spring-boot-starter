package com.yjlan.async.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yjlan
 * @version V1.0
 * @Description 用来表示是哪个业务渠道
 * @date 2022.02.23 15:51
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Channel {
    
    /**
     * 对应的channel的值
     * @return channel的值
     */
    String value() default "";
}
