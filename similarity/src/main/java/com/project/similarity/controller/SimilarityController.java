package com.project.similarity.controller;

import com.project.similarity.controller.requests.CheckTwoRequest;
import com.project.similarity.controller.response.PostResponse;
import com.project.similarity.controller.response.SuccessCheckTwoResponse;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.service.FileService;
import com.project.similarity.utils.models.FileDiff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.project.similarity.utils.ComparisonUtils.showDiff;

@RestController
@RequestMapping("/similarity")
public class SimilarityController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/check-two-models", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> checkTwoModels(@RequestBody CheckTwoRequest request) {
        File fileOne = fileService.getById(request.getFileOne());
        File fileTwo = fileService.getById(request.getFileTwo());

        //Number result = compareTwoModels(fileOne, fileTwo);

        //FileDiff resultDiff = showDiff(fileOne, fileTwo);

        SuccessCheckTwoResponse response = new SuccessCheckTwoResponse();
        response.setSimilarity(0.0);
        //response.setFileDiff(resultDiff);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
