package com.project.similarity.controller.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadZipRequest {
    MultipartFile file;
}
