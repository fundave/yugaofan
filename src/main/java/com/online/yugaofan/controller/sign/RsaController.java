package com.online.yugaofan.controller.sign;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.online.yugaofan.domain.base.BaseResponse;
import com.online.yugaofan.domain.sign.rsa.RsaBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * rsa类
 */
@Slf4j
@RestController
@RequestMapping("/sign/rsa")
public class RsaController {

    private static final String ALGORITHM = "RSA";
    private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
    private static final String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";

    /***
     * RSA生成密钥对
     * @return 结果
     */
    @GetMapping(value = "/{size}")
    public BaseResponse generateRSA(@PathVariable("size") Integer size, @PathParam("isFormat") Integer isFormat) throws NoSuchAlgorithmException {
        Map<String, String> keyPairMap =  new HashMap<>();
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
        // 初始化密钥对生成器
        keyPairGen.initialize(size, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        if (null == isFormat || 0 == isFormat) {
            // 格式化数据放回
            //公钥
            keyPairMap.put("publicKeyString", formatToPem(BEGIN_PUBLIC_KEY, END_PUBLIC_KEY, publicKeyString));
            //私钥
            keyPairMap.put("privateKeyString", formatToPem(BEGIN_RSA_PRIVATE_KEY, END_RSA_PRIVATE_KEY, privateKeyString));
        } else {
            //公钥
            keyPairMap.put("publicKeyString", publicKeyString);
            //私钥
            keyPairMap.put("privateKeyString", privateKeyString);
        }
        return BaseResponse.ok(keyPairMap);
    }

    /***
     * Rsa 加密
     * @param rsaBase 参数
     * @return 结果
     */
    @PostMapping(value = "/encrypt")
    public BaseResponse encrypt(@RequestBody RsaBase rsaBase) {
        if ("0".equals(rsaBase.getFormatStr())) {
            rsaBase.setPublicKeyStr(rsaBase.getPublicKeyStr().replace(BEGIN_PUBLIC_KEY,"").replace(END_PUBLIC_KEY,"").replace("\\r\\n","").replace("\\r","").replace("\\n",""));
        }
        RSA rsa = StrUtil.isBlank(rsaBase.getAlgorithm()) ? new RSA(null, rsaBase.getPublicKeyStr()) : new RSA(rsaBase.getAlgorithm(), null, rsaBase.getPublicKeyStr());
        return BaseResponse.ok("", rsa.encryptBase64(rsaBase.getText(), KeyType.PublicKey));
    }

    /***
     * Rsa 解密
     * @param rsaBase 参数
     * @return 结果
     */
    @PostMapping(value = "/decrypt")
    public BaseResponse decrypt(@RequestBody RsaBase rsaBase) {
        if ("0".equals(rsaBase.getFormatStr())) {
            rsaBase.setPrivateKeyStr(rsaBase.getPrivateKeyStr().replace(BEGIN_RSA_PRIVATE_KEY,"").replace(END_RSA_PRIVATE_KEY,"").replace("\\r\\n",""));
        }
        RSA rsa = StrUtil.isBlank(rsaBase.getAlgorithm()) ? new RSA(rsaBase.getPrivateKeyStr(), null) : new RSA(rsaBase.getAlgorithm(), rsaBase.getPrivateKeyStr(), null);
        return BaseResponse.ok("", rsa.decryptStr(rsaBase.getText(), KeyType.PrivateKey));
    }

    /**
     * 格式化为pem格式
     * @return 结果
     */
    public String formatToPem(String heard, String footer, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(heard);
        stringBuilder.append("\r\n");
        int length = 64;
        int n = (str.length() + length - 1) / length;
        for (int i = 0; i < n; i++) {
            if (i < (n - 1)) {
                stringBuilder.append(str, i * length, (i + 1) * length);
            } else {
                stringBuilder.append(str.substring(i * length));
            }
            stringBuilder.append("\r\n");
        }
        stringBuilder.append(footer);
        return stringBuilder.toString();
    }

    /***
     * Rsa 私钥签名
     * @param rsaBase 参数
     * @return 结果
     */
    @PostMapping(value = "/sign")
    public BaseResponse sign(@RequestBody RsaBase rsaBase) {
        if ("0".equals(rsaBase.getFormatStr())) {
            rsaBase.setPrivateKeyStr(rsaBase.getPrivateKeyStr().replace(BEGIN_RSA_PRIVATE_KEY,"").replace(END_RSA_PRIVATE_KEY,"").replace("\\r\\n",""));
        }
        Sign sign = SecureUtil.sign(generateSignAlgorithm(rsaBase.getAlgorithm()), rsaBase.getPrivateKeyStr(), null);
        //签名
        byte[] data = rsaBase.getText().getBytes(StandardCharsets.UTF_8);
        byte[] signed = sign.sign(data);
        return BaseResponse.ok("", Base64.getEncoder().encodeToString(signed));
    }

    /***
     * Rsa 公密验证签名
     * @param rsaBase 参数
     * @return 结果
     */
    @PostMapping(value = "/checkSign")
    public BaseResponse checkSign(@RequestBody RsaBase rsaBase) {
        try {
            if ("0".equals(rsaBase.getFormatStr())) {
                rsaBase.setPublicKeyStr(rsaBase.getPublicKeyStr().replace(BEGIN_PUBLIC_KEY,"").replace(END_PUBLIC_KEY,"").replace("\\r\\n","").replace("\\r","").replace("\\n",""));
            }
            Sign sign = SecureUtil.sign(generateSignAlgorithm(rsaBase.getAlgorithm()), null, rsaBase.getPublicKeyStr());
            //签名
            byte[] data = rsaBase.getText().getBytes(StandardCharsets.UTF_8);
            // 公密为验证的签名
            boolean verify = sign.verify(data, Base64.getDecoder().decode(rsaBase.getPrivateKeyStr().getBytes(StandardCharsets.UTF_8)));
            return BaseResponse.ok("", verify ? "验证成功" : "验证失败");
        } catch (Exception e) {
            return BaseResponse.ok("", "验证失败");
        }
    }


    private SignAlgorithm generateSignAlgorithm(String algorithmStr) {
        SignAlgorithm algorithm = SignAlgorithm.SHA256withRSA;
        switch (algorithmStr) {
            case "SHA1withRSA":
                algorithm = SignAlgorithm.SHA1withRSA;
                break;
            case "SHA256withRSA":
                algorithm = SignAlgorithm.SHA256withRSA;
                break;
            case "SHA384withRSA":
                algorithm = SignAlgorithm.SHA384withRSA;
                break;
            case "SHA512withRSA":
                algorithm = SignAlgorithm.SHA512withRSA;
                break;
        }
        return algorithm;
    }


}
