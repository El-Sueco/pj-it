package com.project.similarity.db.service;

import com.project.similarity.db.entity.Department;
import com.project.similarity.db.entity.Type;
import com.project.similarity.db.repository.DepartmentRepository;
import com.project.similarity.db.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() {
        List<Department> list = new ArrayList<>();
        departmentRepository.findAll().forEach(list::add);
        return list;
    }

    public Department getById(Long departmentId){
        return departmentRepository.findById(departmentId).get();
    }
}
