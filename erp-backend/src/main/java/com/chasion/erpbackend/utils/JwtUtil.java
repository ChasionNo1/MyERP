package com.chasion.erpbackend.utils;

import com.chasion.erpbackend.entities.User;
import com.chasion.erpbackend.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import java.util.Date;

@Component
public class JwtUtil {

    private static final long ACCESS_EXPIRE_TIME = 15 * 60 * 1000; // 15分钟
    private static final long REFRESH_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000; // 7天

    private static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static String secretString = Encoders.BASE64.encode(key.getEncoded());
    @Autowired
    private UserService userService;

    // 生成AccessToken
    public static String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("position", user.getPosition())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretString)
                .compact();
    }

    // 生成RefreshToken
    public static String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretString)
                .compact();
    }

    // 解析Token
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretString)
                .parseClaimsJws(token)
                .getBody();
    }

    // 验证Token是否过期
    public static boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}