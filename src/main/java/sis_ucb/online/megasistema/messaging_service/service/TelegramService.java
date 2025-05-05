package sis_ucb.online.megasistema.messaging_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class TelegramService {

    private final TelegramClient telegramClient;

    // Aquí inyectamos el TelegramClient, no el TelegramBot
    public TelegramService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public void sendTo(String identifier, String text) {
        SendMessage msg = SendMessage.builder()
                .chatId(identifier.toString())
                .text(text)
                .build();

        // Ahora sí podemos llamar a execute()
        try {
            telegramClient.execute(msg);
        } catch (TelegramApiException e) {
            // TODO Auto-generated catch block
            System.out.println("Error al enviar el mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendBatch(List<String> identifiers, String text) {
        identifiers.parallelStream().forEach(identifier -> {
            SendMessage msg = SendMessage.builder()
                    .chatId(identifier)
                    .text(text)
                    .build();

            try {
                telegramClient.execute(msg);
            } catch (TelegramApiException e) {
                System.out.println("Error al enviar el mensaje a " + identifier + ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
}
