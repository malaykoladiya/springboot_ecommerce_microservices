package com.malaykoladiya.micro_inventory_service.service;


import com.malaykoladiya.micro_inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class InventoryServiceUnitTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsInStock_ReturnsTrue_WhenStockIsAvailable() {
        // Arrange
        String skuCode = "SKU123";
        int quantity = 10;
        when(inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity)).thenReturn(true);

        // Act
        boolean result = inventoryService.isInStock(skuCode, quantity);

        // Assert
        assertTrue(result);
        verify(inventoryRepository, times(1)).existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

    @Test
    void testIsInStock_ReturnsFalse_WhenStockIsNotAvailable() {
        // Arrange
        String skuCode = "SKU123";
        int quantity = 10;
        when(inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity)).thenReturn(false);

        // Act
        boolean result = inventoryService.isInStock(skuCode, quantity);

        // Assert
        assertFalse(result);
        verify(inventoryRepository, times(1)).existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }
}
