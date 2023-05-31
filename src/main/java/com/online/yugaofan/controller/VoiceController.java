package com.online.yugaofan.controller;

import com.online.yugaofan.domain.base.BaseResponse;
import com.online.yugaofan.domain.entity.Ttsmp3;
import com.online.yugaofan.service.VoiceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 文字转语音
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/voice")
public class VoiceController {

    final VoiceService voiceService;

    /***
     * 文字转语音
     * @param ttsmp3 参数
     * @return 结果
     */
    @PostMapping(value = "/toMp3")
    public BaseResponse textToMp3(@RequestBody Ttsmp3 ttsmp3) {
        return BaseResponse.ok(voiceService.textToMp3(ttsmp3));
    }

}
