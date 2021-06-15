package com.project.similarity.db.repository;

import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    public List<File> getAllByAufgabe(Aufgabe aufgabe);
    public Optional<File> getByNameAndAufgabe(String name, Aufgabe aufgabe);
}
