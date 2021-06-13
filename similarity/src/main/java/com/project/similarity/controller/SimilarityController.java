package com.project.similarity.controller;

import com.project.similarity.controller.requests.AufgabeRequest;
import com.project.similarity.controller.requests.CheckTwoRequest;
import com.project.similarity.controller.response.PostResponse;
import com.project.similarity.controller.response.SuccessCheckTwoResponse;
import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Similarity;
import com.project.similarity.db.service.AufgabeService;
import com.project.similarity.db.service.FileService;
import com.project.similarity.db.service.SimilarityService;
import com.project.similarity.utils.models.FileDiff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.project.similarity.utils.ComparisonUtils.cosineSimilarity;
import static com.project.similarity.utils.ComparisonUtils.showDiff;

@RestController
@RequestMapping("/similarity")
public class SimilarityController {

    @Autowired
    private FileService fileService;
    @Autowired
    private AufgabeService aufgabeService;
    @Autowired
    private SimilarityService similarityService;

    @RequestMapping(value = "/all-by-aufgabe/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Similarity>> checkTwoModels(@PathVariable String id) {
        List<Similarity> similarities;
        try {
            Aufgabe aufgabe = aufgabeService.getById(Long.valueOf(id));
            similarities = similarityService.getByAufgabe(aufgabe);
        } catch (Exception e) {
            similarities = new ArrayList<>();
        }
        return new ResponseEntity<>(similarities, HttpStatus.OK);
    }

    @RequestMapping(value = "/show-difference/{id}", method = RequestMethod.GET)
    public ResponseEntity<SuccessCheckTwoResponse> checkTwoModelsDiff(@PathVariable String id) throws IOException {
        Similarity similarity = similarityService.getById(Long.valueOf(id));
        FileDiff diff = showDiff(Paths.get(similarity.getFile1().getPath()), Paths.get(similarity.getFile2().getPath()));
        SuccessCheckTwoResponse response = new SuccessCheckTwoResponse();
        response.setFileDiff(diff);
        response.setSimilarity(similarity.getScore());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
