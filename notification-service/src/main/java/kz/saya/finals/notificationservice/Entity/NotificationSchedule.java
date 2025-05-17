package kz.saya.finals.notificationservice.Entity;

import jakarta.persistence.*;
import kz.saya.finals.common.Enums.NotificationType;
import kz.saya.finals.notificationservice.Converter.MapToJsonConverter;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "notification_schedule")
@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationSchedule extends MappedSuperClass {
    @ManyToOne
    @JoinColumn(name = "template_id")
    private MessageTemplate template;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String recipient;

    private LocalDateTime lastSendAt;

    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> params;

    private boolean repeat;

    private LocalDateTime repeatUntil;

    private int repeatInterval; // в минутах

    private boolean enabled;
}

