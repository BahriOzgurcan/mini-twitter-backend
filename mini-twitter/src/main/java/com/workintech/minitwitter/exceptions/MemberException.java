package com.workintech.minitwitter.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class MemberException extends RuntimeException {
    private HttpStatus code;

    public MemberException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}
