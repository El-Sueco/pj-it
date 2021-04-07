package com.project.similarity.controller;

import com.project.similarity.db.entity.File;
import com.project.similarity.db.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.FuzzyScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/test-lib/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Integer> test(@PathVariable Long id1, @PathVariable Long id2){
        FuzzyScore score = new FuzzyScore(Locale.GERMAN);
        Integer scoreInt = score.fuzzyScore(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        return new ResponseEntity<>(scoreInt, HttpStatus.OK);
    }
}
