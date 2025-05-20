package org.example.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.example.common.model.global.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 * 用于统一处理项目中的异常
 * @author 黄湘湘
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
    public BaseResult<Object> handlerException(Exception e,HttpServletRequest request) {
        final Map<String, String> headMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headMap.put(headerName, request.getHeader(headerName));
        }
        log.error("""
                        返回指定的请求头:{}
                        返回这个请求的HTTP方法的名称:{}
                        请求用户的登录信息:{}
                        请求的URL路径后包含字符串:{}
                        访问路径:{}
                        报错信息：{}
                        ===== 请求失败 =====""",
                headMap,
                request.getMethod(),
                request.getRemoteUser(),
                request.getQueryString(),
                request.getRequestURL(),
                e.getMessage());
        // 返回错误响应，包含异常信息

        return BaseResult.error(500, "返回指定的请求头:" + headMap
                + "返回这个请求的HTTP方法的名称:" + request.getMethod()
                + "请求用户的登录信息:" + request.getRemoteUser()
                + "请求的URL路径后包含字符串:" + request.getQueryString()
                + "访问路径:" + request.getRequestURL()
                + "报错信息：" + e.getMessage());
    }
}
