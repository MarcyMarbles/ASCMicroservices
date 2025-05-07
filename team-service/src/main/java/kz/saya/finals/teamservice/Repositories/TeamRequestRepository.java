package kz.saya.finals.teamservice.Repositories;

import kz.saya.finals.teamservice.Entities.TeamRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, UUID> {
    TeamRequest findByTeamIdAndPlayerId(UUID teamId, UUID teamId1);
}