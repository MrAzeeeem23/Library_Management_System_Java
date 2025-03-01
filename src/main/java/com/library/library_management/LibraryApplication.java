package com.library.library_management;

import com.library.library_management.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {
	@Autowired
	private ScraperService scraperService;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}