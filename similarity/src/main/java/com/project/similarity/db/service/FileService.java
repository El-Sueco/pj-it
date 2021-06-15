package com.project.similarity.db.service;

import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public List<File> getAllByAufgabe(Aufgabe aufgabe) {
        return fileRepository.getAllByAufgabe(aufgabe);
    }

    public File getById(Long id) {
        return fileRepository.findById(id).get();
    }

    public File getByNameAndAufgabe(String name, Aufgabe aufgabe) {
        return fileRepository.getByNameAndAufgabe(name, aufgabe).orElse(new File());
    }

    public File save(File file){
        Optional<File> optional = fileRepository.getByNameAndAufgabe(file.getName(), file.getAufgabe());
        if(optional.isPresent()) {
            file = optional.get();
            file.setPath(file.getPath());
        }
        return fileRepository.save(file);
    }

}
