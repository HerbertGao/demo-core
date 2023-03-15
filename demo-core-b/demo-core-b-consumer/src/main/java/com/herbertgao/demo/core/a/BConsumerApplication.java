package com.herbertgao.demo.core.a;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class BConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BConsumerApplication.class, args);
    }

}
