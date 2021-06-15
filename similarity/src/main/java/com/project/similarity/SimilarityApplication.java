package com.project.similarity;

import com.project.similarity.db.service.FilesStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@SpringBootApplication
@EnableScheduling
public class SimilarityApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(SimilarityApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.init();
	}
}
