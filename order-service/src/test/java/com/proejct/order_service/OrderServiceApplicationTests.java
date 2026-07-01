package com.proejct.order_service;

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

import tools.jackson.databind.ObjectMapper;
import com.proejct.order_service.dto.OrderLineItemDto;
import com.proejct.order_service.dto.OrderRequest;
import com.proejct.order_service.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8")
		.withDatabaseName("order_service");

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
	private ObjectMapper objectMapper;
	@Autowired
	private OrderRepository orderRepository;

	@BeforeEach
	void setUp() {
		orderRepository.deleteAll();
	}

	@Test
	void shouldPlaceOrder() throws Exception {
		OrderRequest orderRequest = getOrderRequest();
		String orderRequestString = objectMapper.writeValueAsString(orderRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
				.contentType(APPLICATION_JSON)
				.content(orderRequestString))
			.andExpect(status().isCreated())
			.andExpect(content().string("Order Placed Successfully"));

		assertEquals(1, orderRepository.findAll().size());
	}

	private OrderRequest getOrderRequest() {
		OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
		orderLineItemDto.setSkuCode("iphone_15");
		orderLineItemDto.setPrice(BigDecimal.valueOf(1200));
		orderLineItemDto.setQuantity(1);

		return new OrderRequest(List.of(orderLineItemDto));
	}

}
