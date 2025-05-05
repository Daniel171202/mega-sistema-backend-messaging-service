package sis_ucb.online.megasistema.messaging_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MessagingConfig {

    @Value("${pocketbase.url}")
    private String pocketbaseUrl;

    @Value("${pocketbase.apiKey:}")  // cadena vacía si no existe
    private String pocketbaseApiKey;

    @Bean
    public WebClient pocketbaseWebClient() {
        return WebClient.builder()
                .baseUrl(pocketbaseUrl)
                .defaultHeader("Authorization", "Bearer " + pocketbaseApiKey)
                .build();
    }
}