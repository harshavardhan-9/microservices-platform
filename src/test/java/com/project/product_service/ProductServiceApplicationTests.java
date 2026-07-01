package com.project.product_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.mongodb.MongoDBContainer;

import tools.jackson.databind.ObjectMapper;
import com.project.product_service.dto.ProductRequest;
import com.project.product_service.repository.ProductRepository;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2") {{
		addFixedExposedPort(27018, 27017);
	}};

	static {
		mongoDBContainer.start();
	}

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(APPLICATION_JSON)
				.content(productRequestString))
			.andExpect(status().isCreated());

		assertEquals(1, productRepository.findAll().size());
	}

	@Test
	void shouldGetAllProducts() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(APPLICATION_JSON)
				.content(productRequestString))
			.andExpect(status().isCreated());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].name").value("iPhone 14"))
			.andExpect(jsonPath("$[0].description").value("iPhone 14 with 128GB storage"))
			.andExpect(jsonPath("$[0].price").value(999.99));
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
			.name("iPhone 14")
			.description("iPhone 14 with 128GB storage")
			.price(BigDecimal.valueOf(999.99))
			.build();
	}

}
