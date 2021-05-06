package com.project.similarity.db.repository;

import com.project.similarity.db.entity.Algo;
import com.project.similarity.db.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlgoRepository extends JpaRepository<Algo, Long> {

    List<Algo> getAllByActiveTrue();
}
