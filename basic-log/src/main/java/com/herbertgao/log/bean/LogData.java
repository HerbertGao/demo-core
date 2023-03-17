package com.herbertgao.log.bean;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LogData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 线程LogData对象
     */
    private static final ThreadLocal<LogData> LOG_DATA = new ThreadLocal<>();
    /**
     * 线程StringBuilder对象 主要用于追加字段到最终的content
     */
    private static final ThreadLocal<StringBuilder> CONTENT_BUILDER = new ThreadLocal<>();

    private String traceId;
    /** 应用名 */
    private String appName;
    /** 类型 */
    private String type;
    /** 主机 */
    private String host;
    /** 端口号 */
    private Integer port;
    /** 请求IP */
    private String clientIp;
    /** 请求地址 */
    private String reqUrl;
    /** HTTP请求方法 */
    private String httpMethod;
    /** HTTP请求头部信息 */
    private Object headers;
    /** 操作标签 */
    private String tag;
    /** 方法内容 */
    private String content;
    /** 操作方法 */
    private String method;
    /** 参数 */
    private Object args;
    /** 响应体 */
    private Object respBody;
    /** 操作日期(调用日期) */
    private Date logDate;
    /** 业务处理耗时 */
    private long costTime;
    /**
     * 线程名
     */
    private String threadName = Thread.currentThread().getName();
    /**
     * 线程Id
     */
    private long threadId = Thread.currentThread().getId();
    /**
     * 执行状态 成功(true)/异常(false)  默认失败false
     */
    private boolean success = false;

    public void setLogDate(Date logDate) {
        if (logDate != null) {
            this.logDate = (Date) logDate.clone();
        }
    }


    /**
     * 获取当前线程中的操作日志对象
     *
     * @return Gets the LogData in the current thread
     */
    public static LogData getCurrent() {
        if (LOG_DATA.get() == null) {
            LogData logData = new LogData();
            logData.setTraceId(IdUtil.randomUUID());
            logData.setLogDate(new Date());
            StringBuilder sb = CONTENT_BUILDER.get();
            if (sb == null) {
                CONTENT_BUILDER.set(new StringBuilder());
            }
            LOG_DATA.set(logData);
        }
        return LOG_DATA.get();
    }

    /**
     * 设置当前线程中的操作日志对象
     *
     * @param logData AopLog日志对象
     */
    public static void setCurrent(LogData logData) {
        if (CONTENT_BUILDER.get() != null) {
            logData.setContent(CONTENT_BUILDER.get().toString());
        }
        LOG_DATA.set(logData);
    }

    /**
     * 移除当前线程AopLog日志对象
     */
    public static void removeCurrent() {
        CONTENT_BUILDER.remove();
        LOG_DATA.remove();
    }

    /**
     * 内容记录记录
     *
     * @param step 这里可以使用 该方法记录每一个步骤
     */
    public static void step(String step) {
        StringBuilder sb = CONTENT_BUILDER.get();
        if (sb != null) {
            sb.append(step).append("\n");
            CONTENT_BUILDER.set(sb);
        }
    }

}
