package com.example.ratelimiter.service;

import com.example.ratelimiter.config.Constant;
import com.example.ratelimiter.entity.Counter;
import com.example.ratelimiter.exception.TooManyException;
import com.example.ratelimiter.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FixWindowCounterService {
    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private Helper helper;
    public Counter increase(String key){
        String redisKey = helper.getPrefixTimestamp() + ":" + key;
        if(!template.hasKey(redisKey)){
            // if key doesn't exist, set key with expired time
            template.opsForValue().set(redisKey, "0", 5, Constant.UNIT);
        }
        long count = template.opsForValue().increment(redisKey);
        if(count > Constant.REQUESTS_PER_UNIT){
            // drop request
            throw new TooManyException(String.format("too many request. Max request per unit: %d, current count: %d",
                    Constant.REQUESTS_PER_UNIT, count));
        }
        return Counter.builder()
                .key(redisKey)
                .counter(count)
                .build();
    }

    public Counter get(String key){
        String redisKey = helper.getPrefixTimestamp() + ":" + key;
        String value = template.opsForValue().get(redisKey);
        if(value == null){
            throw new IllegalArgumentException(redisKey + " doesn't exist");
        }
        long count = Long.parseLong(value);
        return Counter.builder()
                .key(redisKey)
                .counter(count)
                .build();
    }
}
