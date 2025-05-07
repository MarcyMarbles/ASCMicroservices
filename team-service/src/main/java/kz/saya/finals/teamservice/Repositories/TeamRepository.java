package kz.saya.finals.teamservice.Repositories;

import kz.saya.finals.teamservice.Entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    List<Team> findAllByCreatorId(UUID creatorId);
}