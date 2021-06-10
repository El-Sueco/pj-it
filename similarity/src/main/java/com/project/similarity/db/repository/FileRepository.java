package com.project.similarity.db.repository;

import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    public List<File> getAllByAufgabe(Aufgabe aufgabe);
}
