package com.project.similarity.db.service;

import com.project.similarity.db.entity.File;
import com.project.similarity.db.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> getAll() {
        List<File> list = new ArrayList<>();
        fileRepository.findAll().forEach(list::add);
        return list;
    }

    public File save(File file){
        return fileRepository.save(file);
    }
}
