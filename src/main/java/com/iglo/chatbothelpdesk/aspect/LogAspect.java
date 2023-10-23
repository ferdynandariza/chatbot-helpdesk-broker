package com.iglo.chatbothelpdesk.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.iglo.chatbothelpdesk.controller.*Controller.*(..))")
    public void controllerPackageMethods(){
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestBody) && args(body)")
    public void withRequestBodyAnnotation(Object body) {
    }

    @Before(value = "withRequestBodyAnnotation(body)", argNames = "body")
    public void logBeforeMethodWithRequestBody(Object body) {
        log.info("Body: {}", body);
    }


    @Before("controllerPackageMethods()")
    public void logBeforeControllerMethodExecution(JoinPoint jp) {
        Object[] args = jp.getArgs();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("Request : [{}] {}", request.getMethod(), request.getRequestURL().toString());
            log.info("Args    : {}", Arrays.toString(args).replace("[", "").replace("]", ""));
        }
    }


    @AfterReturning(pointcut = "controllerPackageMethods()", returning = "response")
    public void logAfterControllerMethodExecution(Object response) {
        log.info("Response: {}", response);
    }

    @AfterThrowing(pointcut = "execution(* com.iglo.chatbothelpdesk.*.*(..))", throwing = "exception")
    public void handleException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().toShortString();
        log.error("Exception in method {}: {}\n{}",
                methodName, exception.getMessage(), exception.getStackTrace());
    }

}
