package com.project.similarity.db.service;

import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Similarity;
import com.project.similarity.db.repository.FileRepository;
import com.project.similarity.db.repository.SimilarityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimilarityService {

    SimilarityRepository similarityRepository;

    public SimilarityService(SimilarityRepository similarityRepository) {
        this.similarityRepository = similarityRepository;
    }

    public Similarity save(Similarity similarity){
        Double score = similarity.getScore();
        Optional<Similarity> optional = similarityRepository.getByFile1AndFile2(similarity.getFile1(), similarity.getFile2());
        if(optional.isPresent()) {
            similarity = optional.get();
            similarity.setScore(score);
        }
        return similarityRepository.save(similarity);
    }

    public List<Similarity> getByAufgabe(Aufgabe aufgabe){
        return similarityRepository.getByAufgabe(aufgabe);
    }

    public Similarity getById(Long id){
        return similarityRepository.getById(id).orElse(null);
    }
}
