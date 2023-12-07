package com.example.ratelimiter.utils;

import com.example.ratelimiter.config.Constant;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Helper {
    /*
    key = timestamp + key
     */
    public String getPrefixTimestamp(){
        long curTime = System.currentTimeMillis(); // ms
        if(Constant.UNIT == TimeUnit.MINUTES){
            // mintues
            return curTime/(1000 * 60) + "";
        }else if (Constant.UNIT == TimeUnit.SECONDS){
            // seconds
            return curTime/1000 + "";
        }
        return curTime + "";
    }

    public String getPreviousPrefixTimestamp(){
        long curTime = System.currentTimeMillis(); // ms
        if(Constant.UNIT == TimeUnit.MINUTES){
            // minutes
            return (curTime/(1000 * 60) - 1) + "";
        }else if (Constant.UNIT == TimeUnit.SECONDS){
            // seconds
            return (curTime/1000 - 1) + "";
        }

        // ms
        return curTime - 1 + "";
    }

    public double getCurrentPercentage(String curPrefixTimestamp){
        long curTime = System.currentTimeMillis();
        if(Constant.UNIT == TimeUnit.MINUTES){
            // minutes
            long prefixMs = Long.parseLong(curPrefixTimestamp) * 60 * 1000;
            double curPercentage = 1.0 *(curTime - prefixMs) / (60 * 1000);

            return curPercentage;
        }else if (Constant.UNIT == TimeUnit.SECONDS){
            // seconds
            long prefixMs = Long.parseLong(curPrefixTimestamp) * 1000;
            double curPercentage = 1.0 *(curTime - prefixMs) / 1000;
            return curPercentage;
        }
        return 1;
    }

    public String buildRedisKey(String timestamp, String key){
        return String.format("%s:%s", timestamp, key);
    }
}
