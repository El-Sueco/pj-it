package com.project.similarity.db.repository;

import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> getAllByTypeIs(Type type);
}
