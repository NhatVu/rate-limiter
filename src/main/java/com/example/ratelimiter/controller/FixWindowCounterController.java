package com.example.ratelimiter.controller;

import com.example.ratelimiter.entity.Counter;
import com.example.ratelimiter.service.FixWindowCounterService;
import com.example.ratelimiter.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fix")
public class FixWindowCounterController {

    @Autowired
    private FixWindowCounterService service;

    @GetMapping("/get")
    public Map<String, Object> get(@RequestParam String key){
        Counter counter = service.get(key);

        Map<String, Object> res = new HashMap<>();
        res.put(key, counter);
        return res;
    }

    @GetMapping("/increase")
    public Map<String, Object> incr(@RequestParam String key){
        Counter counter = service.increase(key);

        Map<String, Object> res = new HashMap<>();
        res.put(key, counter);
        return res;
    }
}
