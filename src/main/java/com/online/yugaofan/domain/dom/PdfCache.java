package com.online.yugaofan.domain.dom;

import lombok.Data;

/**
 * pdf 缓存类
 */
@Data
public class PdfCache {
    /**
     * 转换状态 默认0 初始化 1 成功 2 失败
     */
    private int staus;

    /**
     * 文件下载地址
     */
    private String filePath;

    /**
     * 文件名字
     */
    private String fileName;
}
