package com.project.similarity.controller;

import com.project.similarity.db.entity.Algo;
import com.project.similarity.db.service.AlgoService;
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
@RequestMapping("/algos")
public class AlgoController {

    @Autowired
    AlgoService algoService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Algo>> getAll(){
        List<Algo> types = algoService.getAllActive();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }
}
