package sis_ucb.online.megasistema.messaging_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;
import sis_ucb.online.megasistema.messaging_service.dto.SendBatchRequest;
import sis_ucb.online.megasistema.messaging_service.dto.SendMessageRequest;
import sis_ucb.online.megasistema.messaging_service.service.PocketbaseClient;
import sis_ucb.online.megasistema.messaging_service.service.TelegramService;

@RestController
@RequestMapping("/api/telegram")
public class TelegramApiController {
    private final TelegramService telegramService;
    private final PocketbaseClient pocketbaseClient;

    public TelegramApiController(TelegramService telegramService, PocketbaseClient pocketbaseClient) {
        this.telegramService = telegramService;
        this.pocketbaseClient = pocketbaseClient;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody SendMessageRequest req) {
        Long chatId = pocketbaseClient.findChatIdByIdentifier(req.getId()).block();
        //Boolean permissionCheck = pocketbaseClient.checkUserPermission(chatId, "docente").block();
        //if (permissionCheck == null || !permissionCheck) {
          //  return ResponseEntity.status(403).build(); // Forbidden
       // }
        if (chatId == null) {
            return ResponseEntity.badRequest().build(); // Bad Request
        }
        telegramService.sendTo(chatId.toString(), req.getText());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendBatch")
    public ResponseEntity<Void> sendBatch(@RequestBody SendBatchRequest req) {
        for (String id : req.getId()) {
            Long chatId = pocketbaseClient.findChatIdByIdentifier(id).block();
            //Boolean permissionCheck = pocketbaseClient.checkUserPermission(chatId, "docente").block();
            //if (permissionCheck == null || !permissionCheck) {
              //  return ResponseEntity.status(403).build(); // Forbidden
           // }
            if (chatId == null) {
                return ResponseEntity.badRequest().build(); // Bad Request
            }
            telegramService.sendTo(chatId.toString(), req.getText());
        }
        return ResponseEntity.ok().build();
    }
}