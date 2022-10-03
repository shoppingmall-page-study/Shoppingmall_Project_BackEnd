package com.project.shopping.Error;

import lombok.Getter;

@Getter
public class CustomExcpetion extends  RuntimeException{

    private  ErrorCode errorCode;

    public CustomExcpetion(String message,ErrorCode errorCode ) {
        super(message);
        this.errorCode = errorCode;
    }
}
