package kz.saya.finals.notificationservice.Repository;

import kz.saya.finals.notificationservice.Entity.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, UUID> {
}