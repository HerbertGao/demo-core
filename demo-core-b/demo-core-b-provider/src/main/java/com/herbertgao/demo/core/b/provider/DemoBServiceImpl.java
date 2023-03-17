package com.herbertgao.demo.core.b.provider;

import com.herbertgao.demo.core.b.service.DemoAService;
import com.herbertgao.demo.core.b.service.DemoBService;
import com.herbertgao.log.annotation.AopLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService
public class DemoBServiceImpl implements DemoBService {

    @DubboReference
    private DemoAService demoAService;

    @AopLog
    @Override
    public String sayHello(String name) {
        return demoAService.sayHello(name) + System.lineSeparator() + "Good Bye " + name;
    }

}
