package kz.saya.finals.notificationservice.Repository;

import kz.saya.finals.notificationservice.Entity.NotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationScheduleRepository extends JpaRepository<NotificationSchedule, UUID> {
    List<NotificationSchedule> findByEnabledTrueAndLastSendAtBefore(LocalDateTime now);
}