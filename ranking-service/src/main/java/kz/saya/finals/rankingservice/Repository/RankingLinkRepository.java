package kz.saya.finals.rankingservice.Repository;

import kz.saya.finals.rankingservice.Entity.Rank;
import kz.saya.finals.rankingservice.Entity.RankingLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface RankingLinkRepository extends JpaRepository<RankingLink, UUID>, JpaSpecificationExecutor<RankingLink> {
    RankingLink findByPlayerProfileIdAndRankId(UUID playerProfileId, UUID rankId);

    RankingLink findByPlayerProfileIdAndRankGameId(UUID playerProfileId, UUID rankGameId);

    List<RankingLink> findByPlayerProfileIdInAndRankGameId(List<UUID> playerProfileIds, UUID rankGameId);

    List<RankingLink> findByPlayerProfileId(UUID playerProfileId);
}