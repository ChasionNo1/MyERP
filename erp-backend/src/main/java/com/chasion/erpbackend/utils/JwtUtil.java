package com.chasion.erpbackend.utils;

import com.chasion.erpbackend.entities.User;
import com.chasion.erpbackend.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import jakarta.servlet.http.HttpServletRequest;
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

    // 从请求头中提取Token（支持多种格式）
    public static String extractTokenFromHeader(HttpServletRequest request) {
        // 优先从标准Authorization头获取（Bearer格式）
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // 备选：从自定义头获取（根据实际需求调整）
        String customHeader = request.getHeader("X-Access-Token");
        if (customHeader != null && !customHeader.isEmpty()) {
            return customHeader;
        }

        return null;
    }
}