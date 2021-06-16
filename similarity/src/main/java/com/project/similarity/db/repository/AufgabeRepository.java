package com.project.similarity.db.repository;

import com.project.similarity.db.entity.Aufgabe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AufgabeRepository extends JpaRepository<Aufgabe, Long> {
    public Optional<Aufgabe> getByNameAndZipName(String name, String zipName);
    public Optional<Aufgabe> getByZipName(String name);
}
