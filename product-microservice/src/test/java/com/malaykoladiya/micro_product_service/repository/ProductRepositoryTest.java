package com.malaykoladiya.micro_product_service.repository;

import com.malaykoladiya.micro_product_service.TestcontainersConfiguration;
import com.malaykoladiya.micro_product_service.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(TestcontainersConfiguration.class)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        product = Product.builder()
                .name("Sample Product")
                .description("This is the sample product")
                .price(BigDecimal.valueOf(19.99))
                .skuCode("SAMPLE001")
                .build();
    }


    @Test
    public void ProductRepository_testSaveProduct() {
        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Sample Product");
    }

    @Test
    public void ProductRepository_testFindProductById() {

        Product savedProduct = productRepository.save(product);
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Sample Product");
    }

    @Test
    public void ProductRepository_testDeleteProduct() {

        Product savedProduct = productRepository.save(product);
        productRepository.deleteById(savedProduct.getId());
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertThat(foundProduct).isNotPresent();
    }

    @Test
    public void ProductRepository_testUpdateProduct() {

        Product savedProduct = productRepository.save(product);
        savedProduct.setPrice(BigDecimal.valueOf(29.99));

        Product updatedProduct = productRepository.save(savedProduct);

        assertThat(updatedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(29.99));
    }

    @Test
    public void ProductRepository_testFindAllProduct() {
        Product product1 = Product.builder()
                .name("Sample Product 2")
                .description("This is second Product")
                .price(BigDecimal.valueOf(12.99))
                .skuCode("SAMPLE002")
                .build();


        productRepository.save(product1);
        productRepository.save(product);

        Iterable<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
    }
}
