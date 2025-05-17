package kz.saya.finals.notificationservice.Repository;

import kz.saya.finals.notificationservice.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}