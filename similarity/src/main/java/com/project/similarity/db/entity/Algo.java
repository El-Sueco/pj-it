package com.project.similarity.db.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "algo")
public class Algo {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "friendlyName", nullable = false)
    private String friendlyName;

    @Column(name = "active", nullable = false)
    private Boolean active;
}