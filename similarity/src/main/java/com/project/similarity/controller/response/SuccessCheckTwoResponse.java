package com.project.similarity.controller.response;

import com.project.similarity.utils.models.FileDiff;
import lombok.Data;

@Data
public class SuccessCheckTwoResponse extends PostResponse {
    Number similarity;
    FileDiff fileDiff;
}
