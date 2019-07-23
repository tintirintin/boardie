package com.hsbc.boardie.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Around("execution(public * com.hsbc.boardie..*(..))")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.info("************** ENTER: " + pjp.toString() + " ***Args: " + Arrays.deepToString(pjp.getArgs()));
        Object ret = pjp.proceed();
        LOGGER.info("************** EXIT " + pjp.toString());
        return ret;
    }

    @AfterThrowing(pointcut = "execution(* com.hsbc.boardie..*(..))", throwing = "ex")
    public void logError(Exception ex) {
        LOGGER.error(ex.toString());
        for (StackTraceElement s : ex.getStackTrace()){
            LOGGER.error(s.toString());
        }
    }
}
