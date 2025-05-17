package kz.saya.finals.notificationservice.Service;

import kz.saya.finals.notificationservice.Entity.NotificationSchedule;
import kz.saya.finals.notificationservice.Repository.NotificationScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final NotificationScheduleRepository notificationScheduleRepository;

    public EmailService(JavaMailSender mailSender, NotificationScheduleRepository notificationScheduleRepository) {
        this.mailSender = mailSender;
        this.notificationScheduleRepository = notificationScheduleRepository;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
