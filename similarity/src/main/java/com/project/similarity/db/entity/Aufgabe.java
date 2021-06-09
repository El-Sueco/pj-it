package com.project.similarity.db.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "aufgabe")
public class Aufgabe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}