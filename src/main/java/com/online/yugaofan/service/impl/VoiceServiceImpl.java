package com.online.yugaofan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.online.yugaofan.domain.entity.Ttsmp3;
import com.online.yugaofan.domain.result.Mp3Result;
import com.online.yugaofan.mapper.Ttsmp3Mapper;
import com.online.yugaofan.service.VoiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
@Slf4j
public class VoiceServiceImpl implements VoiceService {

    final Ttsmp3Mapper ttsmp3Mapper;

    /**
     * 文字转语音地址
     */
    final static String url = "https://ttsmp3.com/makemp3_new.php";

    @Override
    public Mp3Result textToMp3(Ttsmp3 ttsmp3) {
        ttsmp3.setSource("ttsmp3");
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(ttsmp3, false, true);
        String rsp = HttpRequest.post(url).contentType("application/x-www-form-urlencoded").form(stringObjectMap).execute().body();
//        {"Error":0,"Speaker":"Zhiyu","Cached":1,"Text":"\u4f60\u597d\u554a","tasktype":"direct","URL":"https:\/\/ttsmp3.com\/created_mp3\/f03a2540120a0bc9c99e79bececa650f.mp3","MP3":"f03a2540120a0bc9c99e79bececa650f.mp3"}
        JSONObject jsonObject = JSON.parseObject(rsp);
        ttsmp3.setResponseData(rsp);
        ttsmp3Mapper.insert(ttsmp3);
        return new Mp3Result(jsonObject.getString("URL"), "https://ttsmp3.com/dlmp3.php?mp3=" + jsonObject.getString("MP3")+ "&location=direct");
    }

}
