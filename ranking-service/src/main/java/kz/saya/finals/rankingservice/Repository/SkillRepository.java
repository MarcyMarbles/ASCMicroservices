package kz.saya.finals.rankingservice.Repository;

import kz.saya.finals.rankingservice.Entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID>, JpaSpecificationExecutor<Skill> {
    Skill findByPlayerIdAndGameId(UUID playerId, UUID gameId);

    List<Skill> findByPlayerIdInAndGameId(Collection<UUID> playerIds, UUID gameId);
}