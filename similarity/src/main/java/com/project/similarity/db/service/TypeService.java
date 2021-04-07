package com.project.similarity.db.service;

import com.project.similarity.db.entity.Type;
import com.project.similarity.db.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeService {

    TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> getAll() {
        List<Type> list = new ArrayList<>();
        typeRepository.findAll().forEach(list::add);
        return list;
    }

    public Type getById(Long typeId){
        return typeRepository.findById(typeId).get();
    }
}
