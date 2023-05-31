package com.online.yugaofan.domain.fanyi.baidu;

import cn.hutool.crypto.digest.MD5;
import lombok.Data;

/**
 * 百度翻译 文本 请求
 */
@Data
public class BaiduFanyiReq {

    /**
     * 请求翻译query
     */
    private String q;
    /**
     * 翻译源语言 可设置 auto
     */
    private String from;

    /**
     * 翻译目标语言
     */
    private String to;

    /**
     * appid
     */
    private String appid;

    /**
     * 随机数
     */
    private String salt;

    /**
     * 签名 appid+q+salt+密钥的MD5值
     */
    private String sign;

    public void generateSign(String Key) {
        this.sign = MD5.create().digestHex(this.appid + this.q + this.salt + Key);
    }
}
