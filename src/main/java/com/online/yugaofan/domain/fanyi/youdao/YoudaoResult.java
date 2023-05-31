package com.online.yugaofan.domain.fanyi.youdao;

import lombok.Data;

import java.util.List;

/**
 * 有道翻译响应结果
 */
@Data
public class YoudaoResult {
    private String code;
    private String message;
    private List<YoudaoData> data;
}
