package com.online.yugaofan.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 文字转语音
 */
@Data
@TableName("t_ttsmp3")
public class Ttsmp3 {

    /**
     * 输入文字
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 输入文字
     */
    @NotBlank(message = "请输入文字")
    private String msg;

    /**
     * 语言
     */
    @NotBlank(message = "请选择语言")
    private String lang;

    /**
     * 默认ttsmp3
     */
    private String source;

    /**
     * 响应数据
     */
    @TableField(value = "response_data")
    private String responseData;

}
