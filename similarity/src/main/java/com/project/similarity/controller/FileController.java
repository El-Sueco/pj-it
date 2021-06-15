package com.project.similarity.controller;

import com.project.similarity.controller.requests.AufgabeRequest;
import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Similarity;
import com.project.similarity.db.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.project.similarity.utils.ComparisonUtils.cosineSimilarity;


@RestController
@Slf4j
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;
    @Autowired
    AufgabeService aufgabeService;
    @Autowired
    FilesStorageService filesStorageService;
    @Autowired
    SimilarityService similarityService;
    @Autowired
    HealingService healingService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<File>> getAll(){
        List<File> types = fileService.getAll();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @RequestMapping(value = "/all-by-aufgabe", method = RequestMethod.POST)
    public ResponseEntity<List<Similarity>> getAllByAufgabe(AufgabeRequest request) throws IOException {
        Aufgabe aufgabe = aufgabeService.getById(Long.valueOf(request.getAufgabe()));
        List<File> files = fileService.getAllByAufgabe(aufgabe);

        List<Similarity> response = new ArrayList<>();

        for (File file : files) {
            for (File fileCompare : files) {
                if(file.getId() < fileCompare.getId()){
                    Double score = cosineSimilarity(Paths.get(file.getPath()), Paths.get(fileCompare.getPath()));
                    log.info("f1: " + file.getName() + " with f2: " + fileCompare.getName() + " has score: " + score);
                    Similarity similarity = new Similarity();
                    similarity.setFile1(file);
                    similarity.setFile2(fileCompare);
                    similarity.setScore(score);
                    similarity.setAufgabe(aufgabe);
                    similarity = similarityService.save(similarity);

                    response.add(similarity);
                }
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/upload-zip", method = RequestMethod.POST)
    public ResponseEntity<String> uploadZip(@RequestParam("file") MultipartFile file){
        String message;
        try {
            filesStorageService.unzip(file);

            message = String.format("Uploaded the file successfully: %s", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    private void selfHealingCron() throws IOException {
        healingService.selfHealingLogic();
    }

    @ExceptionHandler
    public void handleException(NoSuchFileException ex) throws IOException {
        healingService.selfHealingLogic();
    }
}
