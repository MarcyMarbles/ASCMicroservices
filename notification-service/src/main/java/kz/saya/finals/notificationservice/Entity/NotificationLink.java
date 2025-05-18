package kz.saya.finals.notificationservice.Entity;

import jakarta.persistence.Entity;
import kz.saya.finals.common.Enums.NotificationType;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
/**
 * Данная сущность сделана для того чтобы юзер мог зарегистрировать себя для получения уведомлений
 * Он может зарегестрировать свою почту, и получить туда уведомление о старте турнира, если шаблон был подготовлен со schedul'ером
 * Так же он может перейти по ссылке бота, и условно скинув ему код который ему выдаст система, он сохранит этот чат как recipient*/
@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationLink extends MappedSuperClass {
    private UUID userId; // ID пользователя, которому разрешено отправлять уведомления
    private NotificationType type; // Как он зареган, может быть несколько на одного пользователя
    private String link; // Может быть чат Telegram, или же Email

}
