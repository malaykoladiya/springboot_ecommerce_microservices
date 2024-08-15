package com.malaykoladiya.micro_product_service.service;

import com.malaykoladiya.micro_product_service.dto.ProductRequest;
import com.malaykoladiya.micro_product_service.dto.ProductResponse;
import com.malaykoladiya.micro_product_service.model.Product;
import com.malaykoladiya.micro_product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class ProductServiceUnitTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = Product.builder()
                .id("1")
                .name("Sample Product")
                .description("This is a sample product")
                .price(BigDecimal.valueOf(19.99))
                .skuCode("SAMPLE001")
                .build();
        productRequest = new ProductRequest("1", "Sample Product", "This is a sample product", "SAMPLE001", BigDecimal.valueOf(19.99));
    }

    @Test
    public void ProductService_testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenAnswer((Answer<Product>) invocation -> {
            Product productToSave = invocation.getArgument(0);
            productToSave.setId("1");
            return productToSave;
        });

        ProductResponse productResponse = productService.createProduct(productRequest);

        assertThat(productResponse.id()).isEqualTo(product.getId());
        assertThat(productResponse.name()).isEqualTo(product.getName());
        assertThat(productResponse.description()).isEqualTo(product.getDescription());
        assertThat(productResponse.price()).isEqualTo(product.getPrice());
        assertThat(productResponse.skuCode()).isEqualTo(product.getSkuCode());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void ProductService_testGetAllProduct() {
        Product product1 = Product.builder()
                .id("2")
                .name("Another Product")
                .description("This is another product")
                .price(BigDecimal.valueOf(29.99))
                .skuCode("SAMPLE002")
                .build();
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product1));

        List<ProductResponse> productResponses = productService.getAllProducts();

        assertThat(productResponses).hasSize(2);
        assertThat(productResponses.get(0).name()).isEqualTo(product.getName());
        assertThat(productResponses.get(1).name()).isEqualTo(product1.getName());

        verify(productRepository, times(1)).findAll();
    }
}
