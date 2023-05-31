package com.online.yugaofan.utils.sign;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.legacy.math.linearalgebra.ByteUtils;

/**
 * SM3算法（类似MD5），不可逆
 *
 * @author liuxb
 * @date 2021年10月24日 下午8:09:01
 */
public class SM3Util {
    /**
     * sm3算法加密
     * @param paramStr  待加密字符串
     * @return 返回加密后，固定长度=32的16进制字符串
     */
    public static String encryptHexString(String paramStr) {
        byte[] resultHash = hash(paramStr.getBytes());
        return ByteUtils.toHexString(resultHash);
    }

    /**
     * 返回长度=32的byte数组
     *
     * @explain 生成对应的hash值
     * @param srcData
     * @return
     */
    private static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 通过密钥进行加密
     *
     * @explain 指定密钥进行加密
     * @param keyBytes   密钥
     * @param srcData    被加密的byte数组
     * @return
     */
    public static byte[] hashByKey(byte[] keyBytes, byte[] srcData) {
        KeyParameter keyParameter = new KeyParameter(keyBytes);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(srcData, 0, srcData.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }

    public static void main(String[] args) {
        String encrypt = encryptHexString("都开始考虑时空裂缝看了看");
        System.out.println(encrypt);
        byte[] hashByKey = hashByKey("SM4_KEY_IV".getBytes(), "I Love You Every Day".getBytes());
        String hexString = ByteUtils.toHexString(hashByKey);
        System.out.println(hexString);
    }

}