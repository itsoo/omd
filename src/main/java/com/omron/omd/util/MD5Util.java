package com.omron.omd.util;

import com.jfinal.plugin.activerecord.Record;

import java.security.MessageDigest;
import java.util.Map;

/**
 * MD5 加密工具
 *
 * @author zxy
 */
public final class MD5Util {

    private static final String SALT = "28805937716";

    /**
     * 加密
     *
     * @param record 数据
     */
    public static void encrypt(Record record) {
        String username = record.getStr("username");
        String password = record.getStr("password");
        record.set("password", encrypt(username + password));
    }

    /**
     * 加密
     *
     * @param map 数据
     */
    public static void encrypt(Map<String, Object> map) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        map.put("password", encrypt(username + password));
    }

    /**
     * 加密
     *
     * @param str 待加密字符串
     * @return 加密后字符串
     */
    public static String encrypt(String str) {
        return md5(md5(str) + SALT);
    }

    /**
     * MD5 加密
     *
     * @param str 待加密字符串
     * @return 加密后字符串
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] bytes = md.digest(str.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 16 进制转换字符串
     *
     * @param bytes byte[]
     * @return String
     */
    private static String toHex(byte[] bytes) {
        final char[] hexDigits = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            ret.append(hexDigits[(b >> 4) & 0x0f]);
            ret.append(hexDigits[b & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * 比较字加密符串
     *
     * @param str 明文字符串
     * @param tar 密文字符串
     * @return boolean
     */
    public static boolean verify(String str, String tar) {
        return tar.equals(encrypt(str));
    }
}
