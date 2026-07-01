package com.proejct.inventory_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.mysql.MySQLContainer;

import com.proejct.inventory_service.model.Inventory;
import com.proejct.inventory_service.repository.InventoryRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryServiceApplicationTests {

	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8")
		.withDatabaseName("inventory_service");

	static {
		mySQLContainer.start();
	}

	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private InventoryRepository inventoryRepository;

	@BeforeEach
	void setUp() {
		inventoryRepository.deleteAll();
	}

	@Test
	void shouldReturnInStock_whenQuantityIsPositive() throws Exception {
		Inventory inventory = new Inventory();
		inventory.setSkuCode("iphone_13");
		inventory.setQuantity(100);
		inventoryRepository.save(inventory);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/iphone_13"))
			.andExpect(status().isOk())
			.andExpect(content().string("In Stock"));
	}

	@Test
	void shouldReturnOutOfStock_whenQuantityIsZero() throws Exception {
		Inventory inventory = new Inventory();
		inventory.setSkuCode("iphone_13_red");
		inventory.setQuantity(0);
		inventoryRepository.save(inventory);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/iphone_13_red"))
			.andExpect(status().isOk())
			.andExpect(content().string("Out of Stock"));
	}

	@Test
	void shouldReturnOutOfStock_whenSkuDoesNotExist() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/does_not_exist"))
			.andExpect(status().isOk())
			.andExpect(content().string("Out of Stock"));
	}

}
