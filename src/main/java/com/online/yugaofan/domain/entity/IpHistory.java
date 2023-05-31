package com.online.yugaofan.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 登入历史
 */
@Data
@TableName("t_ip_history")
public class IpHistory {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long Id;

    /**
     * ip
     */
    private String ipAddress;
}
