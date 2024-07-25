package com.malaykoladiya.micro_product_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malaykoladiya.micro_product_service.dto.ProductRequest;
import com.malaykoladiya.micro_product_service.dto.ProductResponse;
import com.malaykoladiya.micro_product_service.service.ProductService;
//import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;


    private ProductResponse productResponse;
    private ProductRequest productRequest;

    @BeforeEach
    public void setup() {
        productRequest = new ProductRequest("1", "Sample Product", "This is a sample product", "SAMPLE001", BigDecimal.valueOf(19.99));
        productResponse = new ProductResponse("1", "Sample Product", "This is a sample product", "SAMPLE001", BigDecimal.valueOf(19.99));
    }


    @Test
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(productResponse.id())))
                .andExpect(jsonPath("$.name", is(productResponse.name())))
                .andExpect(jsonPath("$.description", is(productResponse.description())))
                .andExpect(jsonPath("$.skuCode", is(productResponse.skuCode())))
                .andExpect(jsonPath("$.price", is(productResponse.price().doubleValue())));

    }

    @Test
    public void testGetAllProducts() throws Exception {
        ProductResponse productResponse1 = new ProductResponse("2", "Another Product", "This is another product", "SAMPLE002", BigDecimal.valueOf(29.99));
        List<ProductResponse> productResponses = Arrays.asList(productResponse,productResponse1);

        when(productService.getAllProducts()).thenReturn(productResponses);

        mockMvc.perform(get("/api/product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(productResponse.id())))
                .andExpect(jsonPath("$[0].name", is(productResponse.name())))
                .andExpect(jsonPath("$.[1].id", is(productResponse1.id())))
                .andExpect(jsonPath("$[1].name", is(productResponse1.name())));
    }
}
