package com.learn.springboot.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RetryAspect {
    int MAX_RETRIES = 3;

    @Around("@annotation(com.learn.springboot.aop.annotations.RetryOperation)")
    public Object retryMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        int numAttempts = 0;
        RuntimeException exception;
        do {
            try {
                log.info("Attempt :: {}", numAttempts);
                return joinPoint.proceed();
            } catch(RuntimeException e) {
                numAttempts++;
                exception = e;
            }
        } while(numAttempts <= MAX_RETRIES);
        throw exception;
    }
}
