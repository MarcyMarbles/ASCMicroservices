package kz.saya.finals.mainservice.Repositories;

import kz.saya.finals.mainservice.Entities.SteamProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SteamProfileRepository extends JpaRepository<SteamProfile, UUID> {
    SteamProfile findBySteamName(String steamName);

    SteamProfile findBySteamId(String steamId);

    SteamProfile findByGamerProfileId(UUID gamerProfileId);
}