package com.chasion.erpbackend.exception;

import com.chasion.erpbackend.resp.ResultCode;

// 自定义业务异常类
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(final Integer code, final String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
