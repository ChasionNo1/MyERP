package com.chasion.erpbackend.controller;

import com.chasion.erpbackend.exception.BusinessException;
import com.chasion.erpbackend.resp.Result;
import com.chasion.erpbackend.resp.ResultCode;
import com.chasion.erpbackend.utils.MailClient;
import com.chasion.erpbackend.utils.MyUtils;
import com.chasion.erpbackend.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailClient mailClient;


    // 响应邮箱验证码的请求
    @GetMapping("/verify/code")
    public Result getVerifyCode(@RequestParam String email) {
        // 先生成验证码
        String code = MyUtils.generateVerifyCode();
        // 防刷策略: 60s内不能重复发送
        String lockKey = RedisKeyUtils.getPrefixVerifyCodeLock(email);
        if (redisTemplate.hasKey(lockKey)) {
            throw new BusinessException(500, "请求过于频繁，请稍后再试");
        }
        // 设置锁定标记（60秒自动过期）
        redisTemplate.opsForValue().set(lockKey, "1", 60, TimeUnit.SECONDS);
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



}
