package com.project.similarity.db.service;

import com.project.similarity.db.entity.Algo;
import com.project.similarity.db.repository.AlgoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlgoService {

    AlgoRepository algoRepository;

    public AlgoService(AlgoRepository algoRepository) {
        this.algoRepository = algoRepository;
    }

    public List<Algo> getAllActive() {
        return new ArrayList<>(algoRepository.getAllByActiveTrue());
    }

    public Algo getById(Long typeId){
        return algoRepository.findById(typeId).get();
    }
}
