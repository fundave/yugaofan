package com.online.yugaofan.domain.sign.sm;

import lombok.Data;

/**
 * 国密加密
 */
@Data
public class SmBase {
    /**
     * 加密文本
     */
    private String test;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 算法
     */
    private String algorithm;

    /**
     * 1 加密 2 解密
     */
    private Integer type;
}
