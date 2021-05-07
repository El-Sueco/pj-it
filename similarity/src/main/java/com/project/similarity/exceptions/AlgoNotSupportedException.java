package com.project.similarity.exceptions;

public class AlgoNotSupportedException extends ValidationException {
    public AlgoNotSupportedException(String message) {
        super(ErrorCode.ALGO_NOT_SUPPORTED, message);
    }
}
