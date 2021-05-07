package com.project.similarity.exceptions;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
    EMPTY_TYPE(1000),
    EMPTY_FILE(1001),
    EMPTY_ALGO(1003),
    TYPE_MISMATCH(1004),
    ALGO_NOT_SUPPORTED(1005);

    public int value;

    ErrorCode(int value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.name() + " (" + this.value + ")";
    }
}
