package sis_ucb.online.megasistema.messaging_service.Controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.objects.User;

import io.github.drednote.telegram.core.annotation.TelegramCommand;
import io.github.drednote.telegram.core.annotation.TelegramController;
import io.github.drednote.telegram.core.annotation.TelegramMessage;
import io.github.drednote.telegram.core.annotation.TelegramPatternVariable;
import io.github.drednote.telegram.core.annotation.TelegramRequest;
import io.github.drednote.telegram.core.request.UpdateRequest;
import io.github.drednote.telegram.response.GenericTelegramResponse;
import io.github.drednote.telegram.response.TelegramResponse;

@TelegramController
public class TelegramBotController {
    
    @TelegramCommand("/start")
    public String onStart(User user) {
        System.out.println("User: " + user);
        return "Hello " + user.getFirstName();
    }    


    @TelegramCommand("/jhes")
    public String clubPinguin(UpdateRequest request) {
        return "Club Pinguin";
    }

    @TelegramMessage
    public String onMessage(UpdateRequest request) {
        return "You sent message with types %s".formatted(request.getMessageTypes());
    }

    @TelegramMessage("My name is {name:.*}")
    public String onPattern(@TelegramPatternVariable("name") String name) {
        return "Hello " + name;
    }

    @TelegramRequest
    public TelegramResponse onAll(UpdateRequest request) {
        return new GenericTelegramResponse("Unsupported command");
    }

    
}
