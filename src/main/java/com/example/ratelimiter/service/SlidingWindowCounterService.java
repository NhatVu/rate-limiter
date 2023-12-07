package com.example.ratelimiter.service;

import com.example.ratelimiter.config.Constant;
import com.example.ratelimiter.entity.Counter;
import com.example.ratelimiter.exception.TooManyException;
import com.example.ratelimiter.utils.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SlidingWindowCounterService {
    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private Helper helper;
    public Counter increase(String key){
        String curPrefixTimestamp = helper.getPrefixTimestamp();
        String redisKey = helper.buildRedisKey(curPrefixTimestamp, key);
        long count = NumberUtils.toLong(template.opsForValue().get(redisKey), -1);

        if(count < 0){
            // if key doesn't exist, set key with expired time
            template.opsForValue().set(redisKey, "0", 5, Constant.UNIT);
        }

        // previous
        String preRedisKey = helper.buildRedisKey(helper.getPreviousPrefixTimestamp(), key);

        long preCount = NumberUtils.toLong(template.opsForValue().get(preRedisKey), 0);

        double curPercentage = helper.getCurrentPercentage(curPrefixTimestamp);
        double combineCount = Math.ceil(preCount * (1.0 - curPercentage) + count);
        log.info("preCount: {}, curCount: {}, curPecentage: {}, combineCount: {}", preCount, count, curPercentage, combineCount);
        if(combineCount >= Constant.REQUESTS_PER_UNIT){
            // drop request
            throw new TooManyException(String.format("too many request. Combined. Max request per unit: %d, current count: %d",
                    Constant.REQUESTS_PER_UNIT, count));
        }

        count = template.opsForValue().increment(redisKey);
        return Counter.builder()
                .key(redisKey)
                .counter(count)
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
