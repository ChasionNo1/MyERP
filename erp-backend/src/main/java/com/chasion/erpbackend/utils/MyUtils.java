package com.chasion.erpbackend.utils;

import java.security.SecureRandom;

public class MyUtils {

    public static String generateVerifyCode() {
        int i = new SecureRandom().nextInt(999999);
        return String.format("%06d", i);
    }
}
