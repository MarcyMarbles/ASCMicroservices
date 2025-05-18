package kz.saya.finals.notificationservice.Service;

import kz.saya.finals.notificationservice.Entity.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TelegramService {
    @Value("${telegram.bot.token}")
    private String token;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String chatId, String message) {
        String url = "https://api.telegram.org/bot" + token + "/sendMessage";
        restTemplate.postForObject(url, Map.of(
                "chat_id", chatId,
                "text", message
        ), String.class);
    }

    public void sendMessage(Notification notification) {
        sendMessage(notification.getRecipient().getLink(), notification.getContent());
    }
}
