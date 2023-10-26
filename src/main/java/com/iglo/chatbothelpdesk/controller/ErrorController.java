package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> responseException(ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatusCode()).body(WebResponse.<String>builder()
                .status(exception.getStatusCode().value())
                .errors(exception.getReason())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<String>> globalException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WebResponse.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errors("An unexpected error occurred")
                .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<WebResponse<String>> servletException(HttpRequestMethodNotSupportedException exception){
        return ResponseEntity.status(exception.getStatusCode()).body(WebResponse.<String>builder()
                .status(exception.getStatusCode().value())
                .errors(exception.getMessage())
                .build());
    }


}
