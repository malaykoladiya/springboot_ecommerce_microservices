package com.malaykoladiya.micro_order_service.service;

import com.malaykoladiya.micro_order_service.client.InventoryClient;
import com.malaykoladiya.micro_order_service.dto.OrderRequest;
import com.malaykoladiya.micro_order_service.event.OrderPlacedEvent;
import com.malaykoladiya.micro_order_service.model.Order;
import com.malaykoladiya.micro_order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class OrderServiceUnitTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @InjectMocks
    private OrderService orderService;


    @BeforeEach
    public void setuo() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPlaceOrder_ProductInStock_Success() {
        // Arrange
        OrderRequest orderRequest = createOrderRequest();
        when(inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity())).thenReturn(true);

        // Capture the Order object passed to orderRepository.save()
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        // Act
        orderService.placeOrder(orderRequest);

        // Assert
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getOrderNumber()).isNotNull();
        assertThat(savedOrder.getSkuCode()).isEqualTo(orderRequest.skuCode());
        assertThat(savedOrder.getPrice()).isEqualByComparingTo(orderRequest.price());
        assertThat(savedOrder.getQuantity()).isEqualTo(orderRequest.quantity());

        // Verify Kafka interaction
        ArgumentCaptor<OrderPlacedEvent> eventCaptor = ArgumentCaptor.forClass(OrderPlacedEvent.class);
        verify(kafkaTemplate, times(1)).send(eq("order-placed"), eventCaptor.capture());

        OrderPlacedEvent sentEvent = eventCaptor.getValue();
        assertThat(sentEvent.getOrderNumber()).isEqualTo(savedOrder.getOrderNumber());
        assertThat(sentEvent.getEmail()).isEqualTo(orderRequest.userDetails().email());
        assertThat(sentEvent.getFirstName()).isEqualTo(orderRequest.userDetails().firstName() != null ? orderRequest.userDetails().firstName() : "N/A");
        assertThat(sentEvent.getLastName()).isEqualTo(orderRequest.userDetails().lastName() != null ? orderRequest.userDetails().lastName() : "N/A");
    }

    @Test
    public void testPlaceOrder_ProductNotInStock_ThrowsException() {
        // Arrange
        OrderRequest orderRequest = createOrderRequest();
        when(inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.placeOrder(orderRequest));
        assertThat(exception.getMessage()).isEqualTo("Product with SkuCode " + orderRequest.skuCode() + " is not in stock");

        // Verify that save was never called
        verify(orderRepository, never()).save(any(Order.class));

        // Verify that Kafka was never interacted with
        verify(kafkaTemplate, never()).send(anyString(), any(OrderPlacedEvent.class));
    }

    private OrderRequest createOrderRequest() {
        // Create a stub OrderRequest object with necessary fields
        return new OrderRequest(
                1L,
                "ORD-123",
                "SKU-001",
                BigDecimal.valueOf(100),
                2,
                new OrderRequest.UserDetails("John", "Doe", "john.doe@example.com")
        );
    }
}