package com.malaykoladiya.micro_order_service;

import org.springframework.boot.SpringApplication;

public class TestMicroOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MicroOrderServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
