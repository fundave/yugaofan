package com.online.yugaofan.domain.sign.rsa;

import lombok.Data;

/**
 * rsa 请求参数
 */
@Data
public class RsaBase {

    /**
     * 加密解密文本
     */
    private String text;

    /**
     * 使用算法
     */
    private String algorithm;

    /**
     * 私钥
     */
    private String privateKeyStr;

    /**
     * 公钥
     */
    private String publicKeyStr;

    /**
     * 格式 pem 或 Hex
     */
    private String formatStr;

}
