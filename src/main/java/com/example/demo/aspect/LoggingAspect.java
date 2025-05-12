package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger("FILE_LOGGER");

    @Pointcut("execution(* com.example.demo.controller.*.*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* com.example.demo.service.*.*(..))")
    public void serviceMethods() {}

    @Before("controllerMethods() || serviceMethods()")
    public void logMethodCall(JoinPoint jp) {
        String methodName = jp.getSignature().toShortString();
        Object[] args = jp.getArgs();
        logger.info("Method called: {} with args: {}", methodName, args);
    }

    @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
    public void logMethodSuccess(JoinPoint jp, Object result) {
        String methodName = jp.getSignature().toShortString();
        String newResult = result == null ? "null" : result.toString();
        logger.info("Method {} executed successfully. Returned: {}", methodName, newResult);
    }

    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
    public void logMethodException(JoinPoint jp, Exception ex) {
        String methodName = jp.getSignature().toShortString();
        String newEx = ex == null ? "null" : ex.toString();
        logger.error("Method {} isn't executed . Returned: {}", methodName, newEx);

    }
}