package com.online.yugaofan.domain.json;

import lombok.Data;

/**
 * json 签名字符串转换
 */
@Data
public class JsonSignDasa {

    /**
     * json数据
     */
    private String jsonData;

    /**
     * 排序规则 "1" 字典顺序 "2" 字典倒序
     */
    private String sort;

    /**
     * 格式 "1" key=value "2" value
     */
    private String output;

    /**
     * key-value 拼接字符
     */
    private String keyChar;

    /**
     * 字段 拼接字符
     */
    private String valueChar;
}
