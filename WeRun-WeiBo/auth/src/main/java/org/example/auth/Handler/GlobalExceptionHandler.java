package org.example.auth.Handler;

import org.example.common.model.global.BaseResult;
import org.example.common.model.global.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 * 用于统一处理项目中的异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 拦截所有异常
     *
     * @param e 异常对象
     * @return 返回错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus
    public BaseResult handlerException(Exception e) {
        // 打印异常信息，用于调试和追踪
        e.printStackTrace();
        // 返回错误响应，包含异常信息
        return BaseResult.error(HttpStatus.ERROR,"出现预料外的错误："+e.getMessage());
    }
}
