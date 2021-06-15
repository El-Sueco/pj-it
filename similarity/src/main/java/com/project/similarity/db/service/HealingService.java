package com.project.similarity.db.service;

import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HealingService {

    @Autowired
    FileService fileService;
    @Autowired
    AufgabeService aufgabeService;
    @Autowired
    FilesStorageService filesStorageService;
    @Autowired
    SimilarityService similarityService;

    public void selfHealingLogic() throws IOException {
        List<String> reimport = checkPath();
        for (String file : reimport) {
            try {
                filesStorageService.unzip(file);
            } catch (Exception e) {
                Aufgabe aufgabe = aufgabeService.getByZipName(file);
                aufgabeService.delete(aufgabe);
                filesStorageService.deleteFolder(Paths.get(aufgabe.getPath()));
            }
        }
    }

    private List<String> checkPath() {
        List<Aufgabe> aufgaben = aufgabeService.getAll();
        List<File> files = fileService.getAll();
        List<String> missingFiles = new ArrayList<>();
        for (Aufgabe aufgabe : aufgaben) {
            try {
                filesStorageService.load(Paths.get(aufgabe.getPath()));
            } catch (Exception e) {
                missingFiles.add(aufgabe.getZipName());
            }
        }
        for (File file : files) {
            try {
                filesStorageService.load(Paths.get(file.getPath()));
            } catch (Exception e) {
                missingFiles.add(file.getAufgabe().getZipName());
            }
        }
        return missingFiles.stream().distinct().collect(Collectors.toList());
    }
}