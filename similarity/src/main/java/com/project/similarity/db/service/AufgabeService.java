package com.project.similarity.db.service;

import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.repository.AufgabeRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Aufgabe getByName(String name) {
        return aufgabeRepository.getByName(name).orElse(new Aufgabe());
    }

    public Aufgabe save(Aufgabe aufgabe) {
        Optional<Aufgabe> optional = aufgabeRepository.getByName(aufgabe.getName());
        if(optional.isPresent()) {
            aufgabe = optional.get();
            aufgabe.setPath(aufgabe.getPath());
        }
        return aufgabeRepository.save(aufgabe);
    }

    public Aufgabe getByZipName(String name) {
        return aufgabeRepository.getByZipName(name).orElse(null);
    }

    @Transactional
    public void delete(Aufgabe aufgabe) {
        aufgabeRepository.delete(aufgabe);
    }
}
