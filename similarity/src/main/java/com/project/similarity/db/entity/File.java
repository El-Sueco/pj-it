package com.project.similarity.db.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "path", columnDefinition = "text")
    private String path;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="aufgabe_id")
    private Aufgabe aufgabe;
}
