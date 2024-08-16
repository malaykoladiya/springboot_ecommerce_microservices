package com.malaykoladiya.micro_inventory_service.controller;

import com.malaykoladiya.micro_inventory_service.controlelr.InventoryController;
import com.malaykoladiya.micro_inventory_service.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class InventoryControllerUnitTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsInStock_ReturnsTrue_WhenStockIsAvailable() {
        // Arrange
        String skuCode = "SKU123";
        int quantity = 10;
        when(inventoryService.isInStock(skuCode, quantity)).thenReturn(true);

        // Act
        boolean result = inventoryController.isInStock(skuCode, quantity);

        // Assert
        assertEquals(true, result);
        verify(inventoryService, times(1)).isInStock(skuCode, quantity);
    }

    @Test
    void testIsInStock_ReturnsFalse_WhenStockIsNotAvailable() {
        // Arrange
        String skuCode = "SKU123";
        int quantity = 10;
        when(inventoryService.isInStock(skuCode, quantity)).thenReturn(false);

        // Act
        boolean result = inventoryController.isInStock(skuCode, quantity);

        // Assert
        assertEquals(false, result);
        verify(inventoryService, times(1)).isInStock(skuCode, quantity);
    }
}
