package com.project.similarity.db.repository;

import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Similarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimilarityRepository extends JpaRepository<Similarity, Long> {
    public Optional<Similarity> getByFile1AndFile2(File f1, File f2);
}
