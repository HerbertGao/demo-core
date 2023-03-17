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
            log.info("REQUEST_LOG, traceId:{}, tag:{}, requestUrl:{}, params:{}, headers:{}",
                    data.getTraceId(), data.getTag(), data.getReqUrl(), JSON.toJSONString(data.getArgs()), JSON.toJSONString(data.getHeaders()));
            log.info("RESPONSE_LOG, traceId:{}, tag:{}, result:{}, cost:{}, content:{}",
                    data.getTraceId(), data.getTag(), JSON.toJSONString(data.getRespBody()), data.getCostTime(), data.getContent());
        }
    }
}
