package com.project.similarity.controller;

import com.project.similarity.db.entity.Type;
import com.project.similarity.db.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/types")
public class TypeController {

    @Autowired
    TypeService typeService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Type>> getAll(){
        List<Type> types = typeService.getAll();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }
}
