package sis_ucb.online.megasistema.messaging_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import sis_ucb.online.megasistema.messaging_service.service.TelegramService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagingServiceApplicationTests {

    @Mock
    private TelegramClient telegramClient;

    @InjectMocks
    private TelegramService telegramService;

    @Test
    public void testSendTo_Success() throws TelegramApiException {
        // Given
        String chatId = "12345";
        String text = "Test message";

        // When
        telegramService.sendTo(chatId, text);

        // Then
        verify(telegramClient, times(1)).execute(any(SendMessage.class));
    }

    @Test
    public void testSendTo_HandlesException() throws TelegramApiException {
        // Given
        String chatId = "12345";
        String text = "Test message";
        doThrow(new TelegramApiException("Test exception")).when(telegramClient).execute(any(SendMessage.class));

        // When
        telegramService.sendTo(chatId, text);

        // Then
        verify(telegramClient, times(1)).execute(any(SendMessage.class));
        // We're verifying that the exception is handled without being propagated
    }

    @Test
    public void testSendBatch_Success() throws TelegramApiException {
        // Given
        List<String> chatIds = Arrays.asList("12345", "67890");
        String text = "Test batch message";

        // When
        telegramService.sendBatch(chatIds, text);

        // Then
        verify(telegramClient, times(2)).execute(any(SendMessage.class));
    }

    @Test
    public void testSendBatch_HandlesException() throws TelegramApiException {
        // Given
        List<String> chatIds = Arrays.asList("12345", "67890");
        String text = "Test batch message";
        doThrow(new TelegramApiException("Test exception")).when(telegramClient).execute(any(SendMessage.class));

        // When
        telegramService.sendBatch(chatIds, text);

        // Then
        verify(telegramClient, times(2)).execute(any(SendMessage.class));
        // We're verifying that the exception is handled without being propagated
    }
}