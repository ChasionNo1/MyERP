package com.chasion.erpbackend.exception;

import com.chasion.erpbackend.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Result error;

    //    处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return error;
    }
//    处理参数校验错误
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", errorMsg);
    return Result.error(400, errorMsg);
    }

    // 处理所以未捕获异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleAllException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(500, "系统繁忙，请稍后再试");
    }

}
