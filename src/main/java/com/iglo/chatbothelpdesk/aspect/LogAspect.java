package com.iglo.chatbothelpdesk.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;

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

    @Before("controllerPackageMethods()")
    public void logBeforeControllerMethodExecution() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("[" + request.getMethod() + "] " + request.getRequestURL().toString());
//            log.info("Body: " + body.toString());
            log.info("Headers: " + Arrays.toString(Collections.list(request.getHeaderNames()).toArray()));
        }
    }


    @AfterReturning(pointcut = "controllerPackageMethods()", returning = "response")
    public void logAfterControllerMethodExecution(Object response) {
        log.info("Response: " + response);
    }


}
