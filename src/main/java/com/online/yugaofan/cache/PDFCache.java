package com.online.yugaofan.cache;

import com.alibaba.fastjson.JSON;
import com.online.yugaofan.domain.dom.PdfCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

/**
 * pdf缓存类
 */
@Slf4j
public class PDFCache {

    static Map<String, PdfCache> map = new HashMap<>();

    /**
     * 保存参数
     */
    public static void saveParm(String uuid, PdfCache pdfCache) {
        map.put(uuid, pdfCache);
        log.info("缓存参数:{}", JSON.toJSONString(map));
    }

    /**
     * 获取参数
     */
    public static PdfCache getParm(String uuid) {
        return map.get(uuid);
    }

    /**
     * 每一小时 清空数据
     */
    @Scheduled(cron ="0 0 0/1 * * ?")
    public void emptyData() {
        map.clear();
    }

}
