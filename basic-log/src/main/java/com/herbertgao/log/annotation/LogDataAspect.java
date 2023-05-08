package com.herbertgao.log.annotation;

import com.herbertgao.log.bean.AopLogConfig;
import com.herbertgao.log.processor.LogProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
@Aspect
@EnableAspectJAutoProxy
public final class LogDataAspect {

    @Resource
    private LogProcessor logProcessor;


    @Pointcut("@annotation(com.herbertgao.log.annotation.AopLog) || @within(com.herbertgao.log.annotation.AopLog)")
    public void aopLogPointCut() {
    }

    @Around("aopLogPointCut()")
    public Object note(ProceedingJoinPoint point) throws Throwable {
        AopLogConfig config = new AopLogConfig();
        MethodSignature signature = (MethodSignature) point.getSignature();
        AopLog aopLog = signature.getMethod().getAnnotation(AopLog.class);

        if (aopLog == null) {
            aopLog = point.getTarget().getClass().getAnnotation(AopLog.class);
        }

        if (aopLog != null) {
            config.setTag(aopLog.tag());
            config.setHeaders(aopLog.headers());
            config.setArgs(aopLog.args());
            config.setExclude(aopLog.exclude());
            config.setRespBody(aopLog.respBody());
            config.setStackTraceOnErr(aopLog.stackTraceOnErr());
            config.setAsyncMode(aopLog.asyncMode());
        }

        return logProcessor.proceed(config, point);
    }

}
