package com.herbertgao.demo.core.a.provider;

import com.herbertgao.demo.core.a.service.DemoAService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DemoAServiceImpl implements DemoAService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

}
