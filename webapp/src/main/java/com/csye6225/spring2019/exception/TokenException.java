package com.csye6225.spring2019.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenException extends Exception {
    private String message;
    private int errorCode;
    public TokenException(String msg,int errorCode){
        this.message = msg;
        this.errorCode = errorCode;
    }
}
