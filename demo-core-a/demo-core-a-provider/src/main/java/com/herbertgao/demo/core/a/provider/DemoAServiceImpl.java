package com.herbertgao.demo.core.a.provider;

import com.herbertgao.demo.core.b.service.DemoAService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService
public class DemoAServiceImpl implements DemoAService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

}
