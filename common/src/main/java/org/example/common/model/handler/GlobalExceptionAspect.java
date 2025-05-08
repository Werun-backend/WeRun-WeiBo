package org.example.common.model.handler;


import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class GlobalExceptionAspect {

    // 拦截所有包下的方法
    @Pointcut("execution(* org.example.*.*(..))")
    public void allMethods() {}

    // 捕获异常后处理
    @AfterThrowing(pointcut = "allMethods()", throwing = "ex")
    public void handleException(Throwable ex) {
        // 统一处理逻辑（如记录日志、发送告警）
        System.err.println("全局异常捕获: " + ex.getMessage());
    }
}