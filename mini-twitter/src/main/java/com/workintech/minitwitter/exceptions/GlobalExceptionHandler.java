package com.workintech.minitwitter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException runtimeException) {
        String errorMessage = runtimeException.getMessage();
        Throwable cause = runtimeException.getCause();
        String causeMessage = (cause != null) ? cause.toString() : "Neden belirtilmemiş";

        String response = "Hata Mesajı: " + errorMessage;
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
