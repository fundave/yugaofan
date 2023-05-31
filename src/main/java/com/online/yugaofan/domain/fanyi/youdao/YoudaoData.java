package com.online.yugaofan.domain.fanyi.youdao;

import lombok.Data;

/**
 * 有道data
 */
@Data
public class YoudaoData {
    /**
     * 文本内容
     */
    private String origin;
    /**
     * from
     */
    private String from;
    /**
     * to
     */
    private String to;
    /**
     * 编号
     */
    private String index;
    /**
     * 翻译内容
     */
    private String explain;
}
