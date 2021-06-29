package com.learn.springboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(com.learn.springboot.aop.LogMethod)")
    public void logMethodExecution(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        String params = Arrays.toString(joinPoint.getArgs());
        log.info("Method [{}] gets called with parameters {}", method, params);
    }

    @Around("@annotation(com.learn.springboot.aop.LogMethod)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;
        log.info("Execution took [{}ms]", duration);
        return proceed;
    }

}