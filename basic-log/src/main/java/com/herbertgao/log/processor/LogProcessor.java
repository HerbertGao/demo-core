package com.herbertgao.log.processor;

import com.herbertgao.common.enums.LogTypeEnums;
import com.herbertgao.log.bean.AopLogConfig;
import com.herbertgao.log.bean.LogData;
import com.herbertgao.log.collector.DefaultLogCollector;
import com.herbertgao.log.collector.LogCollector;
import com.herbertgao.log.executor.CollectorExecutor;
import com.herbertgao.log.util.DataExtractor;
import com.herbertgao.log.util.SpringElSupporter;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LogProcessor {

    private final String appName;

    private final ApplicationContext applicationContext;
    private final CollectorExecutor collectorExecutor;
    private final LogCollector logCollector;
    private final SpringElSupporter elSupporter = new SpringElSupporter();
    private final Map<Class<? extends LogCollector>, LogCollector> collectors = new HashMap<>();

    public LogProcessor(@Autowired ApplicationContext applicationContext,
                        @Autowired CollectorExecutor collectorExecutor,
                        @Autowired LogCollector logCollector) {
        this.appName = getAppName(applicationContext);

        this.applicationContext = applicationContext;
        this.collectorExecutor = collectorExecutor;
        this.logCollector = logCollector;
    }

    public String getAppName(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String name = environment.getProperty("spring.application.name");
        if (StringUtils.isNotBlank(name)) {
            return name;
        }
        if (StringUtils.isNotBlank(applicationContext.getId())) {
            return applicationContext.getId();
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if ("main".equals(stackTraceElement.getMethodName())) {
                return stackTraceElement.getFileName();
            }
        }
        return applicationContext.getApplicationName();
    }

    public String getAppName() {
        return appName;
    }

    public Object proceed(AopLogConfig config, ProceedingJoinPoint point) throws Throwable {
        try {
            LogData.removeCurrent();
            LogData data = LogData.getCurrent();
            return proceed(config, data, point);
        } finally {
            LogData.removeCurrent();
        }
    }

    private Object proceed(AopLogConfig aopLog, LogData data, ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        boolean success = false;
        try {
            result = point.proceed();
            success = true;
            return result;
        } catch (Throwable throwable) {
            if (aopLog.isStackTraceOnErr()) {
                try (StringWriter sw = new StringWriter(); PrintWriter writer = new PrintWriter(sw, true)) {
                    throwable.printStackTrace(writer);
                    LogData.step("Fail : \n" + sw);
                }
            }
            throw throwable;
        } finally {
            if (!data.isSuccess()) {
                data.setAppName(appName);
                DataExtractor.logHttpRequest(data, aopLog.getHeaders());
                data.setCostTime(System.currentTimeMillis() - data.getLogDate().getTime());
                MethodSignature signature = (MethodSignature) point.getSignature();
                data.setTag(elSupporter.getByExpression(signature.getMethod(), point.getTarget(), point.getArgs(), aopLog.getTag()).toString());
                data.setMethod(signature.getDeclaringTypeName() + "#" + signature.getName());
                if (aopLog.isArgs()) {
                    data.setArgs(DataExtractor.getArgs(signature.getParameterNames(), point.getArgs()));
                }
                if (aopLog.isRespBody()) {
                    data.setRespBody(DataExtractor.getResult(result));
                }
                data.setSuccess(success);
                LogData.setCurrent(data);
                if (aopLog.isAsyncMode()) {
                    collectorExecutor.asyncExecute(selectLogCollector(aopLog.getCollector()), LogData.getCurrent());
                } else {
                    collectorExecutor.execute(selectLogCollector(aopLog.getCollector()), LogData.getCurrent());
                }
            }
        }
    }

    /**
     * 选择一个收集器进行执行
     */
    private LogCollector selectLogCollector(Class<? extends LogCollector> clz) {
        if (clz == DefaultLogCollector.class || clz == null) {
            return logCollector;
        } else {
            LogCollector collector;
            try {
                collector = applicationContext.getBean(clz);
            } catch (Exception e) {
                collector = collectors.get(clz);
                if (collector == null) {
                    collector = BeanUtils.instantiateClass(clz);
                    collectors.put(clz, collector);
                }
            }
            return collector;
        }
    }
}
