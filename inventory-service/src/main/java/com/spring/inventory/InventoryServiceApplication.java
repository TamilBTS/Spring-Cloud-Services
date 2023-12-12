package com.spring.inventory;

import com.spring.inventory.entity.Inventory;
import com.spring.inventory.repo.InventoryRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableDiscoveryClient
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
		System.setProperty("server.port", "8085");
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadData(InventoryRepo inventoryRepo){
		return args -> {
			Inventory inventory
					= Inventory.builder()
					.skuCode("iphone 15")
					.quantity(100)
					.build();
			Inventory inventory2
					= Inventory.builder()
					.skuCode("iphone 15 pro")
					.quantity(0)
					.build();
			inventoryRepo.saveAll(Arrays.asList(inventory, inventory2));
		};
	}
}
