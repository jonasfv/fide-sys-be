package com.arquiweb.fide_sys_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@SpringBootApplication
public class FideSysBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FideSysBeApplication.class, args);
	}
}
