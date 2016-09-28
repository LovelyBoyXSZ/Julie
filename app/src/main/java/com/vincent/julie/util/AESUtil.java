package com.vincent.julie.util;

import android.util.Base64;

import com.vincent.julie.logs.MyLog;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 * 
 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16的倍数位。
 * 
 */
public class AESUtil {
	
	private static final String TAG = "AESOperator";

    private static String ivParameter = "0392039203920300";
    private static AESUtil instance = null;

    private AESUtil() {

    }

    public static AESUtil getInstance() {
        if (instance == null)
            instance = new AESUtil();
        return instance;
    }


	public static String encrypt(String encData, String secretKey, String vector) {
		String result = "";
		if (secretKey == null) {
			return result;
		}
		/*if (secretKey.length() != 16) {
			return result;
		}*/
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] raw = secretKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
			result = Base64.encodeToString(encrypted, Base64.DEFAULT);// 此处使用BASE64做转码。
		} catch (Exception e) {
			MyLog.e(TAG, " encrypt error !"+e);
		}

		return result;
	}

    public static String encrypt(String encData, String secretKey) {
        return encrypt(encData, secretKey, ivParameter);
    }

    public static String decrypt(String sSrc, String key) {
       return decrypt(sSrc, key, ivParameter);
    }
    
    public static String decrypt(String sSrc, String key, String ivs) {
        String result = "";
        if (key == null) {
			return result;
		}
		/*if (key.length() != 16) {
			return result;
		}*/
    	try {
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            result = new String(original, "utf-8");
        } catch (Exception e) {
        	MyLog.e(TAG, " decrypt error !"+ e);
        }
    	return result;
    }
    
    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }
}