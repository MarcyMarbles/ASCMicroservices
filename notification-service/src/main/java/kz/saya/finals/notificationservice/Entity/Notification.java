package kz.saya.finals.notificationservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kz.saya.finals.common.Enums.NotificationType;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification extends MappedSuperClass {
    @ManyToOne
    private NotificationLink recipient;
    private NotificationType type; // EMAIL, TELEGRAM
    private String subject;
    private String content;
    private LocalDateTime scheduledAt;
    private boolean sent;
}
