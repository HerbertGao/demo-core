package com.herbertgao.log.bean;

import com.herbertgao.log.collector.DefaultLogCollector;
import com.herbertgao.log.collector.LogCollector;
import lombok.Data;
import org.springframework.http.HttpHeaders;

@Data
public class AopLogConfig {


    /** 操作标签/错做分类 */
    private String tag;
    /** 记录的headers */
    private String[] headers;
    /** 是否记录请求参数 */
    private boolean args;
    /** 需要忽略的参数 */
    private String[] exclude;
    /** 是否记录响应报文 */
    private boolean respBody;
    /** 发生异常时，是否追加堆栈信息到content */
    private boolean stackTraceOnErr;
    /** 异步模式 */
    private boolean asyncMode;
    /** 指定专门的收集器 */
    private Class<? extends LogCollector> collector;

    public AopLogConfig() {
        this.tag = "default";
        this.headers = new String[]{HttpHeaders.USER_AGENT, HttpHeaders.CONTENT_TYPE};
        this.args = true;
        this.exclude = new String[]{};
        this.respBody = true;
        this.stackTraceOnErr = true;
        this.asyncMode = true;
        this.collector = DefaultLogCollector.class;
    }
}
