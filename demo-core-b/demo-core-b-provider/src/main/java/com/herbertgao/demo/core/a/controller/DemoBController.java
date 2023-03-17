package com.herbertgao.demo.core.a.controller;

import com.herbertgao.log.annotation.HttpLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoBController {

    @HttpLog
    @GetMapping("/test/{name}")
    public String sayHello(@PathVariable String name) {
        return "hello " + name;
    }

}
