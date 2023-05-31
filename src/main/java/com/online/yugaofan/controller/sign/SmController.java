package com.online.yugaofan.controller.sign;

import cn.hutool.core.util.StrUtil;
import com.online.yugaofan.domain.base.BaseResponse;
import com.online.yugaofan.domain.sign.sm.Sm4Sign;
import com.online.yugaofan.domain.sign.sm.SmBase;
import com.online.yugaofan.utils.sign.SM3Util;
import com.online.yugaofan.utils.sign.SM4Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 国密算法
 */
@Slf4j
@RestController
@RequestMapping("/sign/secrets")
public class SmController {

    /***
     * Rsa 加密
     * @param rsaBase 参数
     * @return 结果
     */
    @PostMapping(value = "/sm3")
    public BaseResponse sm3(@RequestBody SmBase rsaBase) {
        return BaseResponse.ok(StrUtil.isBlank(rsaBase.getPwd()) ? SM3Util.encryptHexString(rsaBase.getTest()) : SM3Util.hashByKey(rsaBase.getPwd().getBytes(), rsaBase.getTest().getBytes()));
    }

    /***
     * Rsa 加密解密
     * @param sm4Sign 参数
     * @return 结果
     */
    @PostMapping(value = "/sm4")
    public BaseResponse sm4(@RequestBody Sm4Sign sm4Sign) throws Exception {
//        Mode mode = Mode.CBC;
//        Padding padding = Padding.PKCS5Padding;
//        for (Mode modeDo : Mode.values()) {
//            if (modeDo.name().equals(sm4Sign.getMode())) {
//                mode = modeDo;
//                break;
//            }
//        }
//        for (Padding paddingDo : Padding.values()) {
//            if (paddingDo.name().equals(sm4Sign.getPadding())) {
//                padding = paddingDo;
//                break;
//            }
//        }
        String str = "SM4/" + sm4Sign.getMode() + "/" + sm4Sign.getPadding();

        if (1 == sm4Sign.getType()) {
            return BaseResponse.ok(SM4Util.encryptSm4(str, sm4Sign.getText(), sm4Sign.getKey()));
        } else {
            return BaseResponse.ok(SM4Util.decryptSm4(str, sm4Sign.getText(), sm4Sign.getKey()));
        }
    }
}
