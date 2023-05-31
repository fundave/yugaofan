package com.online.yugaofan.controller.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.online.yugaofan.domain.base.BaseResponse;
import com.online.yugaofan.domain.json.JsonSignDasa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 */
@Slf4j
@RestController
@RequestMapping("/json")
public class JsonController {

    /***
     * json字符串 签名排序
     * @param jsonSignDasa 参数
     * @return 结果
     */
    @PostMapping(value = "/jsonToSignData")
    public BaseResponse jsonToSignData(@RequestBody JsonSignDasa jsonSignDasa) {
        Map map = JSON.parseObject(jsonSignDasa.getJsonData(), Map.class, Feature.OrderedField);
        if (null == jsonSignDasa.getKeyChar() || "null".equals(jsonSignDasa.getKeyChar())) {
            jsonSignDasa.setKeyChar("");
        }
        if (null == jsonSignDasa.getValueChar() || "null".equals(jsonSignDasa.getValueChar())) {
            jsonSignDasa.setValueChar("");
        }
        List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        if ("2".equals(jsonSignDasa.getSort())) {
            // 倒叙
            Collections.reverse(infoIds);
        } else {
            infoIds.sort(Map.Entry.comparingByKey());
        }
        // 构造签名键值对的格式
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> item : infoIds) {
            if ("1".equals(jsonSignDasa.getOutput())) {
                String key = String.valueOf(item.getKey());
                String val = String.valueOf(item.getValue());
                sb.append(key).append(jsonSignDasa.getKeyChar()).append(val).append(jsonSignDasa.getValueChar());
            } else {
                String val = String.valueOf(item.getValue());
                sb.append(val).append(jsonSignDasa.getValueChar());
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return BaseResponse.ok("", sb.toString());
    }
}
