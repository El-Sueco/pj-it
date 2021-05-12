package com.project.similarity.controller.requests;

import lombok.Data;

@Data
public class FileUploadRequest {
    String name;
    Long type;
    String fileName;
    String file;
}
