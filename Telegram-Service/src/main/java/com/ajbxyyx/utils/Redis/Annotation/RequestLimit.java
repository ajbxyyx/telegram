package com.ajbxyyx.utils.Redis.Annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    /**
     * 限制的数量
     */
    int limit();
    /**
     * 限制毫秒
     */
    long ms();

    /**
     * 消息
     * @return
     */
    String msg() default "request too frequently";

}
