package cn.yiidii.framework.annotation;

import java.lang.annotation.*;

/**
 * 忽略请求追踪注解
 * 标记该注解的接口不会记录请求日志
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreRequestTrace {
    
    /**
     * 是否忽略请求追踪
     */
    boolean value() default true;
} 