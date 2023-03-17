package com.herbertgao.log.annotation;

import com.herbertgao.log.collector.DefaultLogCollector;
import com.herbertgao.log.collector.LogCollector;
import org.springframework.http.HttpHeaders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http日志
 *
 * @author HerbertGao
 * @date 2023-03-16
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpLog {

    /**
     * 操作标签/错做分类
     *
     * @return {@link String}
     */
    String tag() default "default";

    /**
     * 记录的headers
     *
     * @return {@link String[]}
     */
    String[] headers() default {HttpHeaders.USER_AGENT, HttpHeaders.CONTENT_TYPE};

    /**
     * 是否记录请求参数
     *
     * @return boolean
     */
    boolean args() default true;

    /**
     * 需要忽略的参数
     *
     * @return {@link String[]}
     */
    String[] exclude() default {};

    /**
     * 是否记录响应报文
     *
     * @return boolean
     */
    boolean respBody() default true;

    /**
     * 是否仅在发生异常时才记录
     *
     * @return boolean
     */
    boolean onlyOnErr() default false;

    /**
     * 发生异常时，是否追加堆栈信息到content
     *
     * @return boolean
     */
    boolean stackTraceOnErr() default true;

    /**
     * 异步模式
     *
     * @return boolean
     */
    boolean asyncMode() default true;

    /**
     * 指定专门的收集器
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link LogCollector}>
     */
    Class<? extends LogCollector> collector() default DefaultLogCollector.class;
}
