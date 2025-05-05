package sis_ucb.online.megasistema.messaging_service.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class PocketbaseClient {
    private final WebClient webClient;

    public PocketbaseClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Boolean> checkUserPermission(Long telegramId, String permiso) {
        String uri = String.format("/api/collections/usuarios/records?filter=telegramId=\"%d\"", telegramId);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(resp -> System.out.println("Response CheckPermission: " + resp))
                .map(resp -> {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> items = (List<Map<String, Object>>) resp.get("items");
                    if (items == null || items.isEmpty()) return false;
                    // Based on the error, "rol" is a String, not a List
                    String rol = (String) items.get(0).get("rol");
                    return rol != null && rol.equals(permiso);
                });

    }

    public Mono<Boolean> updateUserTelegramId(Long telegramId, String email){

        String uri = String.format("/api/collections/usuarios/records?filter=email=\"%s\"", email);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(resp -> System.out.println("Response UpdateTelegramId: " + resp))
                .flatMap(resp -> {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> items = (List<Map<String, Object>>) resp.get("items");
                    if (items == null || items.isEmpty()) return Mono.just(false);
                    String id = (String) items.get(0).get("id");
                    return webClient.patch()
                            .uri("/api/collections/usuarios/records/" + id)
                            .bodyValue(Map.of("telegramId", telegramId))
                            .retrieve()
                            .bodyToMono(Map.class)
                            .map(response -> true)
                            .onErrorReturn(false);
                });
    }

    public Mono<Long> findChatIdByIdentifier(String identifier) {
        String uri = String.format("/api/collections/usuarios/records?filter=id=\"%s\"", identifier);
        
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(resp -> System.out.println("Response: " + resp))
                .flatMap(resp -> {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> items = (List<Map<String, Object>>) resp.get("items");
                    if (items == null || items.isEmpty()) return Mono.error(new IllegalArgumentException("Usuario no encontrado: " + identifier));
                    Object telegramIdObj = items.get(0).get("telegramId");
                    if (telegramIdObj == null) {
                        return Mono.error(new IllegalArgumentException("telegramId no encontrado para el usuario: " + identifier));
                    }
                    
                    try {
                        return Mono.just(Long.parseLong(telegramIdObj.toString()));
                    } catch (NumberFormatException e) {
                        return Mono.error(new IllegalArgumentException("telegramId no válido: " + telegramIdObj));
                    }
                });
    }

}

