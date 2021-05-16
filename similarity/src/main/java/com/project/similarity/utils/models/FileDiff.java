package com.project.similarity.utils.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FileDiff {
    List<String> fileOne;
    List<String> fileTwo;
}
