package com.project.similarity.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.similarity.db.entity.Department;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.service.DepartmentService;
import com.project.similarity.db.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.FuzzyScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<File>> getAll(){
        List<File> types = fileService.getAll();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @RequestMapping(value = "/test-lib", method = RequestMethod.GET)
    public ResponseEntity<Integer> test(){
        FuzzyScore score = new FuzzyScore(Locale.GERMAN);
        Integer scoreInt = score.fuzzyScore("2222", "0");
        return new ResponseEntity<>(scoreInt, HttpStatus.OK);
    }
}
