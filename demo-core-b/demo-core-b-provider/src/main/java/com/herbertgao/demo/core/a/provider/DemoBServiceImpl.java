package com.herbertgao.demo.core.a.provider;

import com.herbertgao.demo.core.a.service.DemoAService;
import com.herbertgao.demo.core.a.service.DemoBService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService
public class DemoBServiceImpl implements DemoBService {

    @DubboReference
    private DemoAService demoAService;

    @Override
    public String sayHello(String name) {
        return demoAService.sayHello(name) + System.lineSeparator() + "Good Bye " + name;
    }

}
