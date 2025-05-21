package com.chasion.erpbackend.interceptors;

import com.chasion.erpbackend.utils.JwtUtil;
import com.chasion.erpbackend.utils.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;


@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 跳过OPTIONS请求（跨域预检请求）
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        // 2. 从请求头中提取Token
        String token = JwtUtil.extractTokenFromHeader(request);
        // 3. 检查Token是否存在
        if (token == null) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "缺少token");
            return false;
        }
        try {
            Claims claims = JwtUtil.parseToken(token);
            // 5. 解析用户信息并存储到请求上下文中
            Long userId = Long.valueOf(claims.getSubject());
            ThreadLocalUtil.set(userId);
            return true;
        }catch (SignatureException | MalformedJwtException e) {
            // 8. 不支持的Token类型
            setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                    "不支持的Token类型");
            return false;
        }catch (IllegalArgumentException e) {
            // 9. 参数错误（如Token为空）
            setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                    "Token参数异常");
            return false;
        }
    }
//    / 设置错误响应（JSON格式）
    private void setErrorResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"code\":" + status + ",\"message\":\"" + message + "\"}");
        } catch (IOException e) {
            logger.error("设置响应错误", e);
        }
    }
}
