package com.herbertgao.log.collector;

import com.alibaba.fastjson2.JSON;
import com.herbertgao.common.enums.LogTypeEnums;
import com.herbertgao.log.bean.LogData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultLogCollector implements LogCollector {

    @Override
    public void collect(LogData data) {
        if (LogTypeEnums.HTTP.getType().equals(data.getType())) {
            log.info("HTTP_REQUEST_LOG, traceId:{}, tag:{}, requestUrl:{}, params:{}, headers:{}",
                    data.getTraceId(), data.getTag(), data.getReqUrl(), JSON.toJSONString(data.getArgs()), JSON.toJSONString(data.getHeaders()));
            log.info("HTTP_RESPONSE_LOG, traceId:{}, tag:{}, result:{}, cost:{}, content:{}",
                    data.getTraceId(), data.getTag(), JSON.toJSONString(data.getRespBody()), data.getCostTime(), data.getContent());
        } else if (LogTypeEnums.DUBBO.getType().equals(data.getType())) {
            log.info("DUBBO_REQUEST_LOG, traceId:{}, tag:{}, method:{}, params:{}",
                    data.getTraceId(), data.getTag(), data.getMethod(), JSON.toJSONString(data.getArgs()));
            log.info("DUBBO_RESPONSE_LOG, traceId:{}, tag:{}, result:{}, cost:{}, content:{}",
                    data.getTraceId(), data.getTag(), JSON.toJSONString(data.getRespBody()), data.getCostTime(), data.getContent());
        }
    }
}
