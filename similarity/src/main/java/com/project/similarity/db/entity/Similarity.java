package com.project.similarity.db.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "similarity")
public class Similarity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="file1_id")
    private File file1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="file2_id")
    private File file2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="aufgabe_id")
    private Aufgabe aufgabe;

    @Column(name = "score", nullable = false)
    private Double score;
}
