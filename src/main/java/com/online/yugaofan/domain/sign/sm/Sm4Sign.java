package com.online.yugaofan.domain.sign.sm;

import lombok.Data;

/**
 * sm4 国密数据
 */
@Data
public class Sm4Sign {

    private String mode;
    private String padding;
    private String key;
    private String iv;
    private String text;
    private Integer type;
}
