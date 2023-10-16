package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.WebResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> responseException(ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().errors(exception.getReason()).build());
    }

}
