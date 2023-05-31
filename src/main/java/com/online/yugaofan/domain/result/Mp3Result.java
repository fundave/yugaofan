package com.online.yugaofan.domain.result;

import lombok.Data;

/**
 * mp3返回对象
 */
@Data
public class Mp3Result {
    private String readUrl;
    private String downloadenUrl;

    public Mp3Result(String readUrl, String downloadenUrl) {
        this.readUrl = readUrl;
        this.downloadenUrl = downloadenUrl;
    }
}
