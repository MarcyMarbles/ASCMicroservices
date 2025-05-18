package kz.saya.finals.notificationservice.Repository;

import kz.saya.finals.common.Enums.NotificationType;
import kz.saya.finals.notificationservice.Entity.NotificationLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface NotificationLinkRepository extends JpaRepository<NotificationLink, UUID> {
  NotificationLink findByUserId(UUID userId);

  List<NotificationLink> findAllByUserIdIn(List<UUID> teamMemberUserIDs);

  List<NotificationLink> findAllByUserIdInAndTypeEquals(Collection<UUID> userIds, NotificationType type);
}