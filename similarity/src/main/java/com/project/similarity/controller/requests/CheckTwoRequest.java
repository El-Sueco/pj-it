package com.project.similarity.controller.requests;

import lombok.Data;

@Data
public class CheckTwoRequest {
    Long type;
    Long algo;
    Long fileOne;
    Long fileTwo;
}
