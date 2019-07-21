package com.hsbc.boardie.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Around("execution(public * com.hsbc.boardie.*.*(..))")
    public void logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        LOGGER.info("************** BEFORE");
        proceedingJoinPoint.proceed();
        LOGGER.info("************** AFTER");
    }
}
