package kz.saya.finals.mainservice.Repositories;

import kz.saya.finals.mainservice.Entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AchievementRepository extends JpaRepository<Achievement, UUID> {
}