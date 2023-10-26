package com.iglo.chatbothelpdesk.aspect;

import com.iglo.chatbothelpdesk.model.WebResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.iglo.chatbothelpdesk.controller.*Controller.*(..))")
    public void controllerPackageMethods(){}

    @Pointcut("execution(* com.iglo.chatbothelpdesk.controller.ErrorController.*(..))")
    public void errorControllerMethods(){}

    @Pointcut("execution(* com.iglo.chatbothelpdesk..*.*(..)) && !controllerPackageMethods()")
    public void allMethodsExceptInController(){}


    @Before("controllerPackageMethods() && !errorControllerMethods()")
    public void logBeforeControllerMethodExecution(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("Request : [{}] {}", request.getMethod(), request.getRequestURL().toString());
            log.info("Args    : {}", Arrays.toString(args).replace("[", "").replace("]", ""));
        }
    }

    @AfterReturning(pointcut = "controllerPackageMethods()", returning = "response")
    public void logAfterControllerMethodsExecution(WebResponse<?> response) {
        log.info("Response: {}", response);
    }

    @AfterReturning(pointcut = "errorControllerMethods()", returning = "response")
    public void logAfterErrorControllerMethodExecution(ResponseEntity<WebResponse<String>> response) {
        log.info("Response: {}", response.getBody());
    }

    @Before("allMethodsExceptInController()")
    public void logAllMethodExecution(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().toShortString();
        log.debug("Execute method {} with arguments {}", methodName, Arrays.toString(args));
    }

    @AfterThrowing(pointcut = "allMethodsExceptInController()", throwing = "exception")
    public void handleException(JoinPoint joinPoint, Exception exception) {
        if (!(exception instanceof ResponseStatusException)){
            String methodName = joinPoint.getSignature().toShortString();
            log.error("Exception in method {}", methodName, exception);
        }
    }

}
