package com.malaykoladiya.micro_order_service;

import com.malaykoladiya.micro_order_service.client.InventoryClient;
import com.malaykoladiya.micro_order_service.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import static org.hamcrest.MatcherAssert.assertThat;



@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class MicroOrderServiceApplicationTests {

	@LocalServerPort
	private Integer port;


	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
				{
					"skuCode": "Iphone_15",
					"price": 1000,
					"quantity": 1 
				}
				""";

		InventoryClientStub.stubInventoryCall("Iphone_15", 1);

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("Order has been placed Successfully"));
	}

}
