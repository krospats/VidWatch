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
        logger.info("Method called: {} with args: {}",
                jp.getSignature().toShortString(),
                jp.getArgs());
    }

    @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
    public void logMethodSuccess(JoinPoint jp, Object result) {
        logger.info("Method {} executed successfully. Returned: {}",
                jp.getSignature().toShortString(),
                result != null ? result.toString() : "void");
    }

    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
    public void logMethodException(JoinPoint jp, Exception ex) {
        logger.error("Exception in method {}: {}",
                jp.getSignature().toShortString(),
                ex.getMessage(), ex);
    }
}