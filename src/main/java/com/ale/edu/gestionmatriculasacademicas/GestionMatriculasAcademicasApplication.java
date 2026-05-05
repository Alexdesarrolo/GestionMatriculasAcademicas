package com.ale.edu.gestionmatriculasacademicas;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionMatriculasAcademicasApplication {
	private static final Logger log = LoggerFactory.getLogger(GestionMatriculasAcademicasApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GestionMatriculasAcademicasApplication.class, args);
	}

	@PostConstruct
	public void debugAll() {
		log.info("ENV USER: {}", System.getenv("SPRING_DATASOURCE_USERNAME"));
        log.info("PROP USER: {}", System.getProperty("spring.datasource.username"));
	}

}
