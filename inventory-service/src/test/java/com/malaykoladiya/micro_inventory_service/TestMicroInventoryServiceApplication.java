package com.malaykoladiya.micro_inventory_service;

import org.springframework.boot.SpringApplication;

public class TestMicroInventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MicroInventoryServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
