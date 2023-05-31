package com.online.yugaofan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author:  dave
 * @date: 2021/6/15 11:05
 * 
 */
@Component
public class WxConfig {
    public static String appId;
    public static String secret;

    private void setAppId(String appId) {
        WxConfig.appId = appId;
    }

    private void setSecret(String secret) {
        WxConfig.secret = secret;
    }

}
