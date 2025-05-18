package kz.saya.finals.notificationservice.DTOs;

import kz.saya.finals.common.Enums.NotificationType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class NotificationRequestDto {
    private NotificationType type;
    private String content;
    private List<UUID> recipient;

}