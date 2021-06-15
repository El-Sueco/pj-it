package com.project.similarity.controller.response;

import lombok.Data;

@Data
public class ErrorResponse extends PostResponse {
    private String message;
}
