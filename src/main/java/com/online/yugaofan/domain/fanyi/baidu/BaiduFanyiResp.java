package com.online.yugaofan.domain.fanyi.baidu;

import cn.hutool.crypto.digest.MD5;
import lombok.Data;

import java.util.List;

/**
 * 百度翻译 文本 响应
 */
@Data
public class BaiduFanyiResp {

    /**
     * 翻译源语言 可设置 auto
     */
    private String from;

    /**
     * 翻译目标语言
     */
    private String to;

    /**
     * 结果
     */
    private List<BaiduFanyiData> trans_result;

    /**
     * 错误码
     */
    private String error_code;

}
