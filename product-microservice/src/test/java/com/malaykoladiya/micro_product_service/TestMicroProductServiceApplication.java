package com.malaykoladiya.micro_product_service;

import org.springframework.boot.SpringApplication;

public class TestMicroProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MicroProductServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
