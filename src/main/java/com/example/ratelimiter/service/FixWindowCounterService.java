package com.example.ratelimiter.service;

import com.example.ratelimiter.config.Constant;
import com.example.ratelimiter.entity.Counter;
import com.example.ratelimiter.exception.TooManyException;
import com.example.ratelimiter.utils.Helper;
import org.apache.commons.lang3.math.NumberUtils;
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
        String redisKey = helper.buildRedisKey(helper.getPrefixTimestamp(), key);
//        if(!template.hasKey(redisKey)){
//            // if key doesn't exist, set key with expired time
//            template.opsForValue().set(redisKey, "0", 5, Constant.UNIT);
//        }

        long getCount = NumberUtils.toLong(template.opsForValue().get(redisKey), -1);
        if(getCount < 0){
            // key doesn't exist => go into a new timestamp
            template.opsForValue().set(redisKey, "0", 5, Constant.UNIT);
        }
        if(getCount >= Constant.REQUESTS_PER_UNIT){
            // drop request
            throw new TooManyException(String.format("too many request. Max request per unit: %d, current count: %d",
                    Constant.REQUESTS_PER_UNIT, getCount));
        }

        long curCount = template.opsForValue().increment(redisKey);
        return Counter.builder()
                .key(redisKey)
                .counter(curCount)
                .build();
    }

    public Counter get(String key){
        String redisKey = helper.buildRedisKey(helper.getPrefixTimestamp(), key);
        long count = NumberUtils.toLong(template.opsForValue().get(redisKey), -1);

        if(count < 0){
            throw new IllegalArgumentException(redisKey + " doesn't exist");
        }

        return Counter.builder()
                .key(redisKey)
                .counter(count)
                .build();
    }
}
