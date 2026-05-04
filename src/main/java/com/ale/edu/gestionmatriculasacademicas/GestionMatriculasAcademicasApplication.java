package com.ale.edu.gestionmatriculasacademicas;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionMatriculasAcademicasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionMatriculasAcademicasApplication.class, args);
	}

	@PostConstruct
	public void debugAll() {
		System.out.println("ENV USER: " + System.getenv("SPRING_DATASOURCE_USERNAME"));
		System.out.println("PROP USER: " + System.getProperty("spring.datasource.username"));
	}

}
