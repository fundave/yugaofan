package com.online.yugaofan.domain.fanyi.baidu;

import lombok.Data;

/**
 * 翻译结果对象
 */
@Data
public class BaiduFanyiData {
    /**
     * 源文件
     */
    private String src;
    /**
     * 目标
     */
    private String dst;
}
