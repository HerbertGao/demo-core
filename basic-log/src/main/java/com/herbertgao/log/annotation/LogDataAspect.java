package com.herbertgao.log.annotation;

import com.herbertgao.log.bean.HttpLogConfig;
import com.herbertgao.log.processor.LogProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Aspect
@EnableAspectJAutoProxy
public final class LogDataAspect {

    @Resource
    private LogProcessor logProcessor;


    @Pointcut("@annotation(HttpLog) || @within(HttpLog)")
    public void httpLogPointCut() {
    }

    @Around("httpLogPointCut()")
    public Object note(ProceedingJoinPoint point) throws Throwable {
        HttpLogConfig config = new HttpLogConfig();
        MethodSignature signature = (MethodSignature) point.getSignature();
        HttpLog httpLog = signature.getMethod().getAnnotation(HttpLog.class);

        if (httpLog == null) {
            httpLog = point.getTarget().getClass().getAnnotation(HttpLog.class);
        }

        if (httpLog != null) {
            config.setTag(httpLog.tag());
            config.setHeaders(httpLog.headers());
            config.setArgs(httpLog.args());
            config.setExclude(httpLog.exclude());
            config.setRespBody(httpLog.respBody());
            config.setOnlyOnErr(httpLog.onlyOnErr());
            config.setStackTraceOnErr(httpLog.stackTraceOnErr());
            config.setAsyncMode(httpLog.asyncMode());
        }

        return logProcessor.proceed(config, point);
    }

}
