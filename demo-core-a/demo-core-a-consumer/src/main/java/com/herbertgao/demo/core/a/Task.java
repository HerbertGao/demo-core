package com.herbertgao.demo.core.a;

import com.herbertgao.demo.core.b.service.DemoAService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Task implements CommandLineRunner {

    @DubboReference
    private DemoAService demoAService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        String result = demoAService.sayHello("test");
        log.info("result={}", result);
    }
}
