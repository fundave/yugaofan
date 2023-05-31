package com.online.yugaofan.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 翻译表
 */
@Data
@TableName("b_fanyi_lang")
public class FanyiLang {
    /**
     * 中文名语言
     */
    private String name;
    /**
     * 百度代号
     */
    private String code;
    /**
     * 有道代号
     */
    @TableField(value = "youdao_code")
    private String youdaoCode;
    /**
     * 排序
     */
    private Integer sort;
}
