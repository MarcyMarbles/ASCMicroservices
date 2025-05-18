package kz.saya.finals.notificationservice.Service;

import kz.saya.finals.common.Enums.NotificationType;
import kz.saya.finals.notificationservice.Entity.Notification;
import kz.saya.finals.notificationservice.Entity.NotificationSchedule;
import kz.saya.finals.notificationservice.Repository.NotificationRepository;
import kz.saya.finals.notificationservice.Repository.NotificationScheduleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final NotificationScheduleRepository notificationScheduleRepository;
    private final TelegramService telegramService;
    private final NotificationRepository notificationRepository;

    public NotificationService(EmailService emailService, NotificationScheduleRepository notificationScheduleRepository, TelegramService telegramService, NotificationRepository notificationRepository) {
        this.emailService = emailService;
        this.notificationScheduleRepository = notificationScheduleRepository;
        this.telegramService = telegramService;
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(Notification notification) {
        try {
            if (notification.getType() == NotificationType.EMAIL) {
                emailService.sendEmail(notification);
            } else if (notification.getType() == NotificationType.TELEGRAM) {
                telegramService.sendMessage(notification);
            }
        } catch (Exception e) {
            notification.setSent(false);
        }
        notification.setSent(true);
        notificationRepository.save(notification);
    }


    @Scheduled(fixedRate = 60000)
    public void dispatchScheduledNotifications() {
        List<NotificationSchedule> notifications = notificationScheduleRepository
                .findByEnabledTrueAndLastSendAtBefore(LocalDateTime.now());

        for (NotificationSchedule notification : notifications) {
            if (notification.getTemplate() == null) continue;

            String message = applyTemplate(notification.getTemplate().getBody(), notification.getParams());

            switch (notification.getType()) {
                case EMAIL -> emailService.sendEmail(notification.getRecipient(), "Notification", message);
                case TELEGRAM -> telegramService.sendMessage(notification.getRecipient(), message);
            }

            handleRepeat(notification);
        }
    }

    private String applyTemplate(String template, Map<String, String> params) {
        if (params == null || params.isEmpty()) return template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }


    private void handleRepeat(NotificationSchedule notification) {
        if (notification.isRepeat()) {
            notification.setLastSendAt(notification.getLastSendAt().plusMinutes(notification.getRepeatInterval()));
            if (notification.getRepeatUntil() != null && notification.getLastSendAt().isAfter(notification.getRepeatUntil())) {
                notification.setRepeat(false);
            }
            notificationScheduleRepository.save(notification);
        } else {
            notification.setEnabled(false);
            notificationScheduleRepository.save(notification);
        }
    }

}
