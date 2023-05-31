package com.online.yugaofan.utils.sign;

import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * SM4国密算法工具类
 * @author dave
 * @date 2021年10月24日 下午7:04:17
 */
public class SM4Util {

    // 在NoPadding模式下需要手动对齐16字节的倍数
    public static byte[] padding(String arg_text) {
        byte[] encrypt = arg_text.getBytes();

        if (encrypt.length % 16 != 0) {
            byte[] padded = new byte[encrypt.length + 16 - (encrypt.length % 16)];
            System.arraycopy(encrypt, 0, padded, 0, encrypt.length);
            encrypt = padded;
        }
        return encrypt;
    }


    public static String encryptSm4(String str, String plaintext, String key) {
        SymmetricCrypto sm4 = new SymmetricCrypto(str, padding(key));
        return sm4.encryptHex(plaintext);
    }

    public static String decryptSm4(String str, String ciphertext, String key) {
        SymmetricCrypto sm4 = new SymmetricCrypto(str, padding(key));
        return sm4.decryptStr(ciphertext);
    }

}

