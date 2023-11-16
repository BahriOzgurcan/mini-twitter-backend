package com.workintech.minitwitter.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TweetException extends RuntimeException {
    private HttpStatus code;

    public TweetException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}