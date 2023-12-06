package com.example.ratelimiter.exception;

public class TooManyException extends RuntimeException{
    public TooManyException(String msg){
        super(msg);
    }

    public TooManyException(String msg, Throwable e){
        super(msg, e);
    }
}
