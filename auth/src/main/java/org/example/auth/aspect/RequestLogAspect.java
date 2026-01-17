package org.example.auth.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 32218
 */
@Slf4j
@Aspect
@Component
public class RequestLogAspect {

    // 定义切点：拦截所有 Controller 层方法
    @Pointcut("execution(* org.example.auth.controller..*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取 HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        if(request==null) {
            throw new RuntimeException("请求为空");
        }
        // 记录请求基本信息
        log.info("===== 请求开始 =====");
        log.info("URL: {}", request.getRequestURL());
        log.info("方法: {}", request.getMethod());
        log.info("IP: {}", request.getRemoteAddr());
        log.info("类路径: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

        // 记录请求头
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        log.info("请求头: {}", headers);

        // 记录请求体（区分 GET/POST）
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && !(args[0] instanceof HttpServletRequest)) {
            log.info("请求体: {}", Arrays.toString(args));
        } else {
            // 处理 GET 请求参数
            request.getParameterMap().forEach((key, value) ->
                log.info("GET参数[{}]: {}", key, Arrays.toString(value)));
        }

        // 执行目标方法
        Object result = joinPoint.proceed();

        log.info("===== 请求结束 =====");
        return result;
    }
}