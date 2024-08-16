package com.malaykoladiya.micro_order_service.repository;


import com.malaykoladiya.micro_order_service.model.Order;
import com.malaykoladiya.micro_order_service.service.OrderService;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class OrderRepositoryUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService; // Assuming you have a service layer that uses OrderRepository

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveOrder() {
        // Arrange
        Order order = new Order();
        order.setOrderNumber("ORD-123");
        order.setSkuCode("SKU-001");
        order.setPrice(BigDecimal.valueOf(100));
        order.setQuantity(2);

        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order savedOrder = orderRepository.save(order);

        // Assert
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getOrderNumber()).isEqualTo("ORD-123");

        // Verify that save was called
        verify(orderRepository, times(1)).save(order);
    }
}