package com.project.similarity.db.service;

import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.repository.AufgabeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AufgabeService {

    AufgabeRepository aufgabeRepository;

    public AufgabeService(AufgabeRepository aufgabeRepository) {
        this.aufgabeRepository = aufgabeRepository;
    }

    public List<Aufgabe> getAll() {
        List<Aufgabe> list = new ArrayList<>();
        aufgabeRepository.findAll().forEach(list::add);
        return list;
    }

    public Aufgabe getById(Long aufgabeId) {
        return aufgabeRepository.findById(aufgabeId).get();
    }

    public Aufgabe save(Aufgabe aufgabe) {
        return aufgabeRepository.save(aufgabe);
    }
}
