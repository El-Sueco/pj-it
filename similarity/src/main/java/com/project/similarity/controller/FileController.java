package com.project.similarity.controller;

import com.project.similarity.controller.requests.AufgabeRequest;
import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Similarity;
import com.project.similarity.db.service.AufgabeService;
import com.project.similarity.db.service.FileService;
import com.project.similarity.db.service.FilesStorageService;
import com.project.similarity.db.service.SimilarityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<File>> getAll(){
        List<File> types = fileService.getAll();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @RequestMapping(value = "/all-by-aufgabe", method = RequestMethod.POST)
    public ResponseEntity<List<Similarity>> getAllByAufgabe(AufgabeRequest request) throws IOException {
        Aufgabe aufgabe = aufgabeService.getById(request.getAufgabe());
        List<File> files = fileService.getAllByAufgabe(aufgabe);

        List<Similarity> response = new ArrayList<>();

        for (File file : files) {
            for (File fileCompare : files) {
                if(!file.getId().equals(fileCompare.getId())){
                    Double score = cosineSimilarity(Paths.get(file.getPath()), Paths.get(fileCompare.getPath()));
                    log.info("f1: " + file.getName() + " with f2: " + fileCompare.getName() + " has score: " + score);
                    Similarity similarity = new Similarity();
                    similarity.setFile1(file);
                    similarity.setFile2(fileCompare);
                    similarity.setScore(score);
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
            filesStorageService.save(file);

            unzip(file.getOriginalFilename());

            filesStorageService.deleteZip(file.getOriginalFilename());
            message = String.format("Uploaded the file successfully: %s", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }


    private void unzip(String file) throws IOException {
        Aufgabe aufgabe = new Aufgabe();

        String fileZip = String.format("src/main/resources/uploads/%s", file);
        java.io.File destDir = new java.io.File("src/main/resources/unzipped");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            java.io.File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                aufgabe.setName(newFile.getName());
                aufgabe.setPath(newFile.getPath());
                aufgabeService.save(aufgabe);
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File dbFile = new File();
                dbFile.setName(newFile.getName());
                dbFile.setPath(newFile.getPath());
                dbFile.setAufgabe(aufgabe);
                fileService.save(dbFile);
                java.io.File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private java.io.File newFile(java.io.File destinationDir, ZipEntry zipEntry) throws IOException {
        java.io.File destFile = new java.io.File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + java.io.File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
