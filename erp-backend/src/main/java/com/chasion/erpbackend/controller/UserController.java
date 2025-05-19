package com.chasion.erpbackend.controller;

import com.chasion.erpbackend.entities.User;
import com.chasion.erpbackend.exception.BusinessException;
import com.chasion.erpbackend.resp.Result;
import com.chasion.erpbackend.resp.ResultCode;
import com.chasion.erpbackend.service.UserService;
import com.chasion.erpbackend.utils.JwtUtil;
import com.chasion.erpbackend.utils.MailClient;
import com.chasion.erpbackend.utils.MyUtils;
import com.chasion.erpbackend.utils.RedisKeyUtils;
import com.google.code.kaptcha.Producer;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private UserService userService;

    @Autowired
    private Producer captchaProducer;

    // 响应邮箱验证码的请求
    @GetMapping("/verify/code")
    public Result getVerifyCode(@RequestParam String email) {
        // 防刷策略: 60s内不能重复发送
        String lockKey = RedisKeyUtils.getPrefixVerifyCodeLock(email);
        if (redisTemplate.hasKey(lockKey)) {
            throw new BusinessException(500, "请求过于频繁，请稍后再试");
        }
        // 设置锁定标记（60秒自动过期）
        redisTemplate.opsForValue().set(lockKey, "1", 60, TimeUnit.SECONDS);
        // 生成验证码
        String code = MyUtils.generateVerifyCode();
        // 存入redis
        String key = RedisKeyUtils.getPrefixVerifyCode(email);
        redisTemplate.opsForValue().set(
                key,
                code,
                5,
                TimeUnit.MINUTES
        );
        // 发送邮件
        String content = "<div style=\"font-family: Arial; color: #333;\">" +
                "  <p>尊敬的用户：</p>" +
                "  <p>您正在注册XXX系统，验证码为：</p>" +
                "  <div style=\"font-size: 24px; color: #1890ff; margin: 15px 0;\">" + code + "</div>" +
                "  <p style=\"color: #999;\">" +
                "    * 有效期5分钟，请勿泄露给他人<br>" +
                "    * 如非本人操作请忽略本邮件" +
                "  </p>" +
                "</div>";
        mailClient.sendMail(email, "注册验证码", content);

        return Result.success(ResultCode.SUCCESS.getMessage());
    }

    // 响应注册请求
    @PostMapping("/register")
    public Result register(@RequestBody HashMap<String, Object> map) {
        // 请求体里有一部分数据了
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String rePassword = map.get("rePassword").toString();
        String email = map.get("email").toString();
        String code = map.get("verifyCode").toString();
        if (username == null || password == null || email == null || code == null) {
            throw new BusinessException(403, "请求参数有误");
        }
        // 检查验证码
        String key = RedisKeyUtils.getPrefixVerifyCode(email);
        if (redisTemplate.hasKey(key)) {
            String saveCode = (String) redisTemplate.opsForValue().get(key);
            if (!saveCode.toLowerCase().equals(code.toLowerCase())) {
                throw new BusinessException(400, "验证码有误");
            }else {
                // 查询这个用户是否注册过
                User byUsername = userService.findByUsername(username);
                if (byUsername != null) {
                    throw new BusinessException(400, "注册用户已存在");
                }
                if (!password.equals(rePassword)) {
                    throw new BusinessException(400, "两次输入密码不一致");
                }
                // 注册逻辑
                userService.register(username, password, email);
            }
        }else {
            throw new BusinessException(400, "验证码已过期");
        }
        return Result.success("注册成功");
    }

    // 响应验证码
    @GetMapping("/verify/image")
    public Result<HashMap<String, Object>> getCaptcha(HttpServletRequest request) {
        // 防刷策略
        // 根据ip地址来，限制在一段时间内多次点击
        String ip = request.getRemoteAddr();
        String lockKey = RedisKeyUtils.getPrefixVerifyImageLock(ip); // 获取IP对应的Redis键
        long now = System.currentTimeMillis(); // 当前时间（毫秒）
        long oneMinuteAgo = now - 60 * 1000; // 1分钟前的时间（毫秒）
        int maxRequests = 20; // 最大请求次数

        // 使用有序集合（ZSET）记录请求时间戳
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        // 1. 添加当前请求时间到ZSET
        zSetOps.add(lockKey, now, now); // score和value均为时间戳，方便范围查询

        // 2. 移除1分钟前的请求记录
        zSetOps.removeRangeByScore(lockKey, 0, oneMinuteAgo);

        // 3. 获取当前有效请求次数
        Long count = zSetOps.size(lockKey);

        // 4. 检查是否超过限制
        if (count != null && count > maxRequests) {
            throw new BusinessException(500, "每分钟最多请求" + maxRequests + "次，请稍后再试");
        }

        // 5. 设置键过期时间（避免内存泄漏）
        redisTemplate.expire(lockKey, 60, TimeUnit.SECONDS);

        String text = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(text);
        String base64Image = MyUtils.imageToBase64(image);
        String uuid = MyUtils.generateCompactUuid(8);
        HashMap<String, Object> map = new HashMap<>();
        map.put("base64Image", base64Image);
        map.put("uuid", uuid);
        redisTemplate.opsForValue().set(
                uuid,
                text,
                5,
                TimeUnit.MINUTES
        );

        return Result.success("获取成功", map);

    }

    // 登录方法
    @PostMapping("/login")
    public Result login(@RequestBody HashMap<String, Object> map, HttpServletResponse response) {
        // 验证参数
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String code = map.get("verifyCode").toString();
        if (username == null || password == null || code == null) {
            throw new BusinessException(403, "用户名或密码错误");
        }
        User byUsername = userService.findByUsername(username);
        if (byUsername == null) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        userService.login(username, password);
        // 生成双token
        String accessToken = JwtUtil.generateAccessToken(byUsername);
        String refreshToken = JwtUtil.generateRefreshToken(byUsername);

        // 将refresh token 存入redis中
        String refreshKey = RedisKeyUtils.getPrefixRefreshTokenKey(byUsername.getId());
        redisTemplate.opsForValue().set(refreshKey, refreshToken, 7, TimeUnit.DAYS);

        // 6. 返回结果（AccessToken通过响应头返回，RefreshToken通过Cookie返回）
        response.setHeader("Authorization", "Bearer " + accessToken);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return Result.success("登录成功");
    }

    // 双重token 更新
    @PostMapping("/refresh/token")
    public Result refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // 1、从cookie中获取refresh token
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            throw new BusinessException(401, "未提供刷新令牌");
        }

        // 2、验证refresh token
        Claims claims = JwtUtil.parseToken(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());
        User user = userService.findById(userId);

        // 3、检查redis中refresh token是否存在
        String refreshTokenKey = RedisKeyUtils.getPrefixRefreshTokenKey(userId);
        String storedToken = (String) redisTemplate.opsForValue().get(refreshTokenKey);
        if (!refreshToken.equals(storedToken)) {
            throw new BusinessException(401, "刷新令牌无效");
        }
        // 4、生成新的access token
        String accessToken = JwtUtil.generateAccessToken(user);
        response.setHeader("Authorization", "Bearer " + accessToken);
        return Result.success("令牌刷新成功");
    }

}
