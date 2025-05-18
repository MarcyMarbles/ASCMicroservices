package kz.saya.finals.rankingservice.Repository;

import kz.saya.finals.rankingservice.Entity.Rank;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface RankRepository extends JpaRepository<Rank, UUID>, JpaSpecificationExecutor<Rank> {
    Rank findByName(String name);
    Rank findByGameIdAndName(UUID gameId, String name);
    Rank findByGameIdAndId(UUID gameId, UUID id);
    Rank findByGameIdAndDescription(UUID gameId, String description);

    Rank findByMinSkillLevelBetween(int minSkillLevelAfter, int minSkillLevelBefore);

    Rank findByMinSkillLevelEquals(int minSkillLevel, Limit limit);

    Rank findByMinSkillLevelEquals(int minSkillLevel, Sort sort, Limit limit);

    Rank findByMinSkillLevelIsGreaterThanEqual(int minSkillLevelIsGreaterThan, Sort sort, Limit limit);
}