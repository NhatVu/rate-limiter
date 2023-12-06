package com.example.ratelimiter.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Counter {
    private String key;
    private long counter;
}
