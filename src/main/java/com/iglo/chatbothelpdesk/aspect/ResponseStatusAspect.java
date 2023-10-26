package com.iglo.chatbothelpdesk.aspect;

import com.iglo.chatbothelpdesk.model.WebResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ResponseStatusAspect {

    @AfterReturning(pointcut = "execution(* com.iglo.chatbothelpdesk.controller.*.*(..))", returning = "response")
    public void SetResponseStatus(JoinPoint joinPoint, Object response){
        if (response instanceof WebResponse<?> webResponse) webResponse.setStatus(HttpStatus.OK.value());
    }


}
