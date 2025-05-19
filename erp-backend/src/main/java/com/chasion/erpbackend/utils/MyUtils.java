package com.chasion.erpbackend.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class MyUtils {

    public static String generateVerifyCode() {
        int i = new SecureRandom().nextInt(999999);
        return String.format("%06d", i);
    }

    // 生成salt
    public static String getSalt(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("盐值长度必须为正整数");
        }

        // 定义字符集：大写字母 + 小写字母 + 数字
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int charSetLength = charSet.length();

        SecureRandom random = new SecureRandom(); // 安全的随机数生成器
        StringBuilder salt = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charSetLength); // 生成0到charSetLength-1的随机索引
            salt.append(charSet.charAt(randomIndex)); // 拼接字符
        }

        return salt.toString();
    }

    // 图片转Base64工具方法
    public static String imageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("生成验证码失败", e);
        }
    }

    // 生成uuid
    public static String generateCompactUuid(int length) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
    }
}
