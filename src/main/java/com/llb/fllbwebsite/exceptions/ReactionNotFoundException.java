package com.llb.fllbwebsite.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReactionNotFoundException extends RuntimeException {
    public ReactionNotFoundException(String message) {
        super(message);
    }
}
