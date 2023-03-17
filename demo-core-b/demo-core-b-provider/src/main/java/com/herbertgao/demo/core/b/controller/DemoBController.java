package com.herbertgao.demo.core.b.controller;

import com.herbertgao.log.annotation.AopLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoBController {

    @AopLog
    @GetMapping("/test/{name}")
    public String sayHello(@PathVariable String name) {
        return "hello " + name;
    }

}
