package com.ddx.util.basis.utils.sm4;

import cn.hutool.core.codec.Base64;
import com.ddx.util.basis.constant.BasisConstant;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

/**
 * @ClassName: SM4Utils
 * @Description: SM4 国密加密
 * @Author: YI.LAU
 * @Date: 2022年05月24日
 * @Version: 1.0
 */
public class SM4Utils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    //============================ SM4Utils对外提供加解密方法 ============================

    /**
     * SM4加密16进制后加密Base64
     * @param hexKey 加密密钥 只支持32位
     * @param paramStr 加密字符
     * @return 返回Base64加密字符串
     */
    public static String encryptBase64 (String hexKey,String paramStr){
        return Base64.encode(encryptEcb(hexKey,paramStr));
    }

    /**
     * 使用系统密钥SM4加密16进制后加密Base64
     * @param paramStr 加密字符
     * @return 返回Base64加密字符串
     */
    public static String encryptBase64 (String paramStr){
        return Base64.encode(encryptEcb(ByteUtils.toHexString(BasisConstant.SM4_KEY.getBytes()),paramStr));
    }

    /**
     * 解密Base64在进行SM4解密
     * @param hexKey 解密密钥 只支持32位
     * @param cipherText 解密字符
     * @return 返回文明字符
     */
    public static String decryptBase64(String hexKey, String cipherText){
        return decryptEcb(hexKey,Base64.decodeStr(cipherText));
    }

    /**
     * 使用系统密钥解密Base64在进行SM4解密
     * @param cipherText 解密字符
     * @return 返回文明字符
     */
    public static String decryptBase64(String cipherText){
        return decryptEcb(ByteUtils.toHexString(BasisConstant.SM4_KEY.getBytes()),Base64.decodeStr(cipherText));
    }

    //============================ 加密部分 ============================

    /**
     * sm4加密 16进制 核心方法
     * @explain 加密模式：ECB 密文长度不固定，会随着被加密字符串长度的变化而变化
     * @param hexKey 16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串,异常时返回null
     */
    private static String encryptEcb(String hexKey, String paramStr){
        String cipherText = null;
        try {
            // 16进制字符串-->byte[]
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            // String-->byte[]
            byte[] srcData = paramStr.getBytes(BasisConstant.ENCODING);
            // 加密后的数组
            byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
            // byte[]-->hexString
            cipherText = ByteUtils.toHexString(cipherArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * 生成ECB暗号加密
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    private static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(BasisConstant.ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    //============================ 解密部分 ============================

    /**
     * sm4解密 核心方法
     * @explain 解密模式：采用ECB
     * @param hexKey 16进制密钥
     * @param cipherText 16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串,异常时返回null
     */
    private static String decryptEcb(String hexKey, String cipherText){
        String decryptStr = null;
        try {
            // hexString-->byte[]
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            // hexString-->byte[]
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            // 解密
            byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
            // byte[]-->String
            decryptStr = new String(srcData, BasisConstant.ENCODING);

        }catch (Exception e){
            e.printStackTrace();
        }
        return decryptStr;
    }

    /**
     * 生成ECB暗号解密
     * @explain
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     */
    private static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText) throws  Exception{
        Cipher cipher = generateEcbCipher(BasisConstant.ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    //============================ SM4配置 ============================

    /**
     * 生成ECB暗号
     * @explain ECB模式（电子密码本模式：Electronic codebook）
     * @param algorithmName 算法名称
     * @param mode 模式
     * @param key
     * @return
     * @throws Exception
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, BasisConstant.ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }
}