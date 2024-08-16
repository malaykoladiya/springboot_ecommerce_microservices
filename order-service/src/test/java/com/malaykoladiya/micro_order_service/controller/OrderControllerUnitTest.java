package com.malaykoladiya.micro_order_service.controller;

import com.malaykoladiya.micro_order_service.dto.OrderRequest;
import com.malaykoladiya.micro_order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class OrderControllerUnitTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPlaceOrder_Success() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest(
                1L, "ORD-123", "SKU-001", BigDecimal.valueOf(100), 2,
                new OrderRequest.UserDetails("test@example.com", "John", "Doe")
        );

        doNothing().when(orderService).placeOrder(any(OrderRequest.class));

        // Act
        String response = orderController.placeOrder(orderRequest);

        // Assert
        assertEquals("Order has been placed Successfully", response);
        verify(orderService, times(1)).placeOrder(orderRequest);
    }

    @Test
    public void testPlaceOrder_ExceptionThrown() {
        // Arrange
        OrderRequest orderRequest = createOrderRequest();
        doThrow(new RuntimeException("Product not in stock"))
                .when(orderService).placeOrder(any(OrderRequest.class));

        // Act & Assert
        try {
            orderController.placeOrder(orderRequest);
        } catch (RuntimeException ex) {
            assertEquals("Product not in stock", ex.getMessage());
        }

        verify(orderService, times(1)).placeOrder(orderRequest);
    }

    // Utility method to create a common OrderRequest object
    private OrderRequest createOrderRequest() {
        return new OrderRequest(
                1L, "ORD-123", "SKU-001", BigDecimal.valueOf(100), 2,
                new OrderRequest.UserDetails("test@example.com", "John", "Doe")
        );
    }
}