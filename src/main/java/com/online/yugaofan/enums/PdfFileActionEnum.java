package com.online.yugaofan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * pdf 文件处理
 */
@Getter
@AllArgsConstructor
public enum PdfFileActionEnum {
    toWordDoc,
    toWordDocx,
    toExcel,
    toImagePng,
    toImageJpg,
    toPPTX,
    ;

    public static PdfFileActionEnum getPdfFileActionEnum(String name) {
        for (PdfFileActionEnum pdfFileActionEnum : PdfFileActionEnum.values()) {
            if (name.equals(pdfFileActionEnum.name())) {
                return pdfFileActionEnum;
            }
        }
        return null;
    }
}
