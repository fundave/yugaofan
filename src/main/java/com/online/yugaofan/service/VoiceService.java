package com.online.yugaofan.service;

import com.online.yugaofan.domain.entity.Ttsmp3;
import com.online.yugaofan.domain.result.Mp3Result;

public interface VoiceService {
    /**
     * 下载MP3
     * @param ttsmp3 参数信息
     * @return 下载地址
     */
    Mp3Result textToMp3(Ttsmp3 ttsmp3);
}
