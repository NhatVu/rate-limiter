package com.example.ratelimiter.config;

import java.util.concurrent.TimeUnit;

public class Constant {
    public static final TimeUnit UNIT;
    private static final String UNIT_STR = "minutes";

    static{
        if("minutes".equals(UNIT_STR)){
            UNIT = TimeUnit.MINUTES;
        }else if("seconds".equals(UNIT_STR)) {
            UNIT = TimeUnit.SECONDS;
        }else {
            UNIT = TimeUnit.MILLISECONDS;
        }
    }
    public static final int REQUESTS_PER_UNIT = 5;
}
