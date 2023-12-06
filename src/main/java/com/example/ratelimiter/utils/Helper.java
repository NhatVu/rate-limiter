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


}
