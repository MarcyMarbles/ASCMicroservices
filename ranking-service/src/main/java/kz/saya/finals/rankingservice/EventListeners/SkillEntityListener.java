package kz.saya.finals.rankingservice.EventListeners;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import kz.saya.finals.rankingservice.Entity.Rank;
import kz.saya.finals.rankingservice.Entity.RankingLink;
import kz.saya.finals.rankingservice.Entity.Skill;
import kz.saya.finals.rankingservice.Repository.RankRepository;
import kz.saya.finals.rankingservice.Repository.RankingLinkRepository;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;

public class SkillEntityListener {
    private final RankRepository rankRepository;
    private final RankingLinkRepository rankingLinkRepository;

    public SkillEntityListener(RankRepository rankRepository, RankingLinkRepository rankingLinkRepository) {
        this.rankRepository = rankRepository;
        this.rankingLinkRepository = rankingLinkRepository;
    }

    @PostPersist
    public void afterInsert(Skill skill) {
        System.out.println("[Skill Listener] Created Skill: " + skill.getPlayerId());
    }

    @PostUpdate
    public void afterUpdate(Skill skill) {
        System.out.println("[Skill Listener] Updated Skill: " + skill.getPlayerId());
        if (skill.getMatchesPlayed() < 10) {
            return; // Ещё не откалиброван
        }
        Rank rank = rankRepository
                .findByMinSkillLevelIsGreaterThanEqual(skill.getSkillLevel(),
                        Sort.by(Sort.Direction.DESC, "minSkillLevel"),
                        Limit.of(1));
        RankingLink rankingLink = rankingLinkRepository.findByPlayerProfileIdAndRankGameId(
                skill.getPlayerId(),
                rank.getGameId());
        if (rankingLink == null) {
            RankingLink newRankingLink = new RankingLink();
            newRankingLink.setRank(rank);
            newRankingLink.setPlayerProfileId(skill.getPlayerId());
            rankingLinkRepository.save(newRankingLink);
            return;
        }
        if (rankingLink.getRank().equals(rank)) {
            return;
        }
        rankingLink.setRank(rank);
        rankingLinkRepository.save(rankingLink);
        return;
    }
}
