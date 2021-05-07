package com.project.similarity.controller;

import com.project.similarity.controller.requests.CheckTwoRequest;
import com.project.similarity.controller.response.CheckTwoResponse;
import com.project.similarity.db.entity.Algo;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Type;
import com.project.similarity.db.service.AlgoService;
import com.project.similarity.db.service.FileService;
import com.project.similarity.db.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.project.similarity.utils.ComparisonUtils.compareTwoModels;

@RestController
@RequestMapping("/similarity")
public class SimilarityController {

    @Autowired
    private AlgoService algoService;
    @Autowired
    private FileService fileService;
    @Autowired
    private TypeService typeService;

    @RequestMapping(value = "/check-two-models", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckTwoResponse> checkTwoModels(@RequestBody CheckTwoRequest request) {
        File fileOne = fileService.getById(request.getFileOne());
        File fileTwo = fileService.getById(request.getFileTwo());
        Type type = typeService.getById(request.getType());
        Algo algo = algoService.getById(request.getAlgo());

        Number result = compareTwoModels(algo, fileOne, fileTwo);

        CheckTwoResponse response = new CheckTwoResponse();
        response.setSimilarity(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
