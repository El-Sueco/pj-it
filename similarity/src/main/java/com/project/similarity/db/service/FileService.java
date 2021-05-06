package com.project.similarity.db.service;

import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Type;
import com.project.similarity.db.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public List<File> getAllByType(Type type) {
        return fileRepository.getAllByTypeIs(type);
    }

    public File getById(Long id) {
        return fileRepository.findById(id).get();
    }

    public File save(File file){
        return fileRepository.save(file);
    }
}
