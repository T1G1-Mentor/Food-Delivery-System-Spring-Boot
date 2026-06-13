package com.mentorship.food_delivery_app.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(public * com.mentorship.food_delivery_app.*.service.implementation..*(..))")
    public Object aroundServices(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("Executing: {} with arguments: {}", methodName, args);

        long startTime = System.currentTimeMillis();

        try {
            // This physically executes the actual method
            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("Finished: {} in {}ms.", methodName, executionTime);

            return result;

        } catch (Throwable ex) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Exception in: {} after {}ms. Error: {}", methodName, executionTime, ex.getMessage());
            throw ex;
        }
    }
}
