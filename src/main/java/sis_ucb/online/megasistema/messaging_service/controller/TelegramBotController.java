package sis_ucb.online.megasistema.messaging_service.controller;

import org.telegram.telegrambots.meta.api.objects.User;

import io.github.drednote.telegram.core.annotation.TelegramCommand;
import io.github.drednote.telegram.core.annotation.TelegramController;
import io.github.drednote.telegram.core.annotation.TelegramMessage;
import io.github.drednote.telegram.core.annotation.TelegramPatternVariable;
import io.github.drednote.telegram.core.annotation.TelegramRequest;
import io.github.drednote.telegram.core.request.UpdateRequest;
import io.github.drednote.telegram.response.GenericTelegramResponse;
import io.github.drednote.telegram.response.TelegramResponse;
import sis_ucb.online.megasistema.messaging_service.service.PocketbaseClient;

@TelegramController
public class TelegramBotController {
    private final PocketbaseClient pocketbaseClient;

    public TelegramBotController(PocketbaseClient pocketbaseClient) {
        this.pocketbaseClient = pocketbaseClient;
    }
    
    @TelegramCommand("/inicio")
    public String alIniciar(User usuario) {
        System.out.println("Usuario: " + usuario);
        return "Hola " + usuario.getFirstName();
    }    

    @TelegramCommand("/inicio {email:.*}")
    public String alIniciarConEmail(@TelegramPatternVariable("email") String email, User usuario) {


        System.out.println("Usuario: " + usuario);

        Boolean resultado = pocketbaseClient.updateUserTelegramId(usuario.getId(), email).block();
        if (resultado == null || !resultado) {
            return "Error al actualizar el ID de Telegram. Por favor, intenta nuevamente.";
        }



        return "Hola " + usuario.getFirstName() + ", tu ID de Telegram ha sido actualizado con el email: " + email;
        
    }
      


    @TelegramCommand("/ayuda")
    public String alAyuda() {
        return "Comandos disponibles:\n" +
               "/inicio - Iniciar una conversación con el bot\n" +
               "/inicio {email} - Iniciar con tu email\n";
    }

    @TelegramRequest
    public TelegramResponse aTodos(UpdateRequest peticion) {
        return new GenericTelegramResponse("Comando no soportado. Prueba /ayuda para ver los comandos disponibles.");
    }
}
