package com.project.similarity.exceptions;

public class TypeMismatchException extends ValidationException {
    public TypeMismatchException (String message) {
        super(ErrorCode.TYPE_MISMATCH, message);
    }
}
