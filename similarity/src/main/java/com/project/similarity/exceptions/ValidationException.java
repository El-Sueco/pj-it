package com.project.similarity.exceptions;

public abstract class ValidationException extends Exception {

    public ErrorCode errorCode;

    public ValidationException(ErrorCode errorCode, String errorMessage){
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
