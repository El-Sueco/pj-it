package com.project.similarity.controller;

import com.project.similarity.controller.requests.FileRequest;
import com.project.similarity.controller.requests.FileUploadRequest;
import com.project.similarity.db.entity.Department;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Type;
import com.project.similarity.db.service.DepartmentService;
import com.project.similarity.db.service.FileService;
import com.project.similarity.db.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;
    @Autowired
    TypeService typeService;
    @Autowired
    DepartmentService departmentService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<File>> getAll(){
        List<File> types = fileService.getAll();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @RequestMapping(value = "/all-by-type", method = RequestMethod.POST)
    public ResponseEntity<List<File>> getAllByType(@RequestBody FileRequest request){
        Type type = typeService.getById(request.getType());
        List<File> types = fileService.getAllByType(type);
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<File> upload(@RequestBody FileUploadRequest request){
        File file = new File();
        file.setName(request.getName());
        // TODO remove C:fakepath from this looks so ugly tho lol
        file.setFileName(request.getFileName());
        file.setContent(request.getFile());
        // TODO we need a form field for dep in FE
        Department department = departmentService.getById(1L);
        file.setDepartment(department);
        Type type = typeService.getById(request.getType());
        file.setType(type);
        fileService.save(file);
        return new ResponseEntity<>(file, HttpStatus.OK);
    }
}
