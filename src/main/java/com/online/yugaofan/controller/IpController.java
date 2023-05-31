package com.online.yugaofan.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.online.yugaofan.domain.base.BaseResponse;
import com.online.yugaofan.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * ip查询
 */
@Slf4j
@RestController
@RequestMapping("/ip")
public class IpController {

//    static final String loadUrl = "https://qifu-api.baidubce.com/ip/local/geo/v1/district?";
    static final String districtUrl = "https://qifu-api.baidubce.com/ip/geo/v1/district?ip=";

    /***
     * 文字转语音
     * @param ip ip
     * @return 结果
     */
    @GetMapping(value = "/district")
    public BaseResponse district(@PathParam(value = "ip") String ip) {
        String url = StrUtil.isNotBlank(ip) ? districtUrl + ip : districtUrl + ServletUtil.getClientIP(ServletUtils.getRequest(), null);
        String body = HttpUtil.get(url);
        log.info(body);
        return BaseResponse.ok(body);
    }
}
