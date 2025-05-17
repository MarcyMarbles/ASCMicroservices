package kz.saya.finals.notificationservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import kz.saya.finals.common.Enums.NotificationType;
import kz.saya.sbasecore.Entity.MappedSuperClass;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification extends MappedSuperClass {
    private String recipient;
    private NotificationType type; // EMAIL, TELEGRAM
    private String subject;
    private String content;
    private LocalDateTime scheduledAt;
    private boolean sent;
}
