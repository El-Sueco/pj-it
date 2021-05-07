package com.project.similarity.controller.response;

import com.project.similarity.exceptions.ErrorCode;
import lombok.Data;

@Data
public class ErrorResponse extends PostResponse {
    private ErrorCode errorCode;
    private String message;
}
