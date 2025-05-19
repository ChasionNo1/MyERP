package com.chasion.erpbackend.utils;

// 设置redis 的key
public class RedisKeyUtils {
    private static final String PREFIX_VERIFY_CODE = "verify:code";
    private static final String PREFIX_VERIFY_IMAGE = "verify:image";
    private static final String PREFIX_VERIFY_CODE_LOCK = "verify:code:lock";
    private static final String PREFIX_VERIFY_IMAGE_LOCK = "verify:image:lock";


    // 向某个邮箱发送验证码
    public static String getPrefixVerifyCode(String email) {
        return PREFIX_VERIFY_CODE + ":" + email;
    }

    public static String getPrefixVerifyCodeLock(String email) {
        return PREFIX_VERIFY_CODE_LOCK + ":" + email;
    }

    public static String getPrefixVerifyImageLock(String ip){
        return PREFIX_VERIFY_IMAGE_LOCK + ":" + ip;
    }
}
