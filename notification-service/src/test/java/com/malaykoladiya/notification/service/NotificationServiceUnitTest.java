package com.malaykoladiya.notification.service;

import com.malaykoladiya.micro_order_service.event.OrderPlacedEvent;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;



public class NotificationServiceUnitTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationService notificationService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEmailSentSuccessfully() {
        // Arrange
        OrderPlacedEvent orderPlacedEvent = createSampleOrderPlacedEvent();
        doNothing().when(javaMailSender).send(any(MimeMessagePreparator.class));

        // Act
        notificationService.lister(orderPlacedEvent);

        // Assert
        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
    }


    @Test
    public void testMailExceptionIsHandled() {
        // Arrange
        OrderPlacedEvent orderPlacedEvent = createSampleOrderPlacedEvent();
        doThrow(new MailException("Mail sending failed") {}).when(javaMailSender).send(any(MimeMessagePreparator.class));

        // Act & Assert
        assertThatThrownBy(() -> notificationService.lister(orderPlacedEvent))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Exception occurred when sending mail to springshop@email.com");

        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
    }

    private OrderPlacedEvent createSampleOrderPlacedEvent() {
        // Create an Avro OrderPlacedEvent object
        return OrderPlacedEvent.newBuilder()
                .setOrderNumber("ORD-123")
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .build();
    }
}
