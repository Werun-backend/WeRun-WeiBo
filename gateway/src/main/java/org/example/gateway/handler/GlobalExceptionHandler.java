package org.example.gateway.handler;

import org.example.common.model.global.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

/**
 * 全局异常处理类
 * 用于统一处理项目中的异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 拦截所有异常
     *
     * @param e 异常对象
     * @return 返回错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus
    public BaseResult<Object> handlerException(Exception e, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        log.error("""
                        返回指定的请求头:{}
                        返回这个请求的HTTP方法的名称:{}
                        请求的URL路径后包含字符串:{}
                        访问路径:{}
                        报错信息：{}
                        ===== 请求失败 =====""",
                request.getHeaders().entrySet(),
                request.getMethod(),
                request.getQueryParams(),
                request.getPath(),
                e.getMessage());
        // 返回错误响应，包含异常信息

        return BaseResult.error(500, "返回指定的请求头:" + request.getHeaders().entrySet()
                + "返回这个请求的HTTP方法的名称:" + request.getMethod()
                + "请求的URL路径后包含字符串:" + request.getQueryParams()
                + "访问路径:" + request.getPath()
                + "报错信息：" + e.getMessage());
    }
}
