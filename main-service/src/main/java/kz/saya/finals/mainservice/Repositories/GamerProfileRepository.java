package kz.saya.finals.mainservice.Repositories;

import kz.saya.finals.mainservice.Entities.GamerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GamerProfileRepository extends JpaRepository<GamerProfile, UUID> {
    GamerProfile findGamerProfileByUserId(UUID userId);
}