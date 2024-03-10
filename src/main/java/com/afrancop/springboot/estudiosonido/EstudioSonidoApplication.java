package com.afrancop.springboot.estudiosonido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.afrancop.springboot.estudiosonido.models.service.IUploadFileService;

@SpringBootApplication
public class EstudioSonidoApplication implements CommandLineRunner {

	@Autowired
	IUploadFileService uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(EstudioSonidoApplication.class, args);
	}

	@Override	
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
	}


}
