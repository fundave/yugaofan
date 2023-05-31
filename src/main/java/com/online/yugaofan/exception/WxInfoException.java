package com.online.yugaofan.exception;

/**
 * @author:  dave
 * @date: 2021/6/15 14:19
 * 
 * 微信相关异常
 */
public class WxInfoException extends RuntimeException{
    public WxInfoException(String message) {
        super(message);
    }

    public WxInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getStatus() {
        return 1001;
    }
}
