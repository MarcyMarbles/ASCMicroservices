package kz.saya.finals.rankingservice.Service;

import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimResultsDto;
import kz.saya.finals.common.DTOs.Scrim.TabInfoDto;
import kz.saya.finals.rankingservice.Entity.Rank;
import kz.saya.finals.rankingservice.Entity.RankingLink;
import kz.saya.finals.rankingservice.Entity.Skill;
import kz.saya.finals.rankingservice.Repository.RankingLinkRepository;
import kz.saya.finals.rankingservice.Repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RankService {
    private final int K = 25;
    private final RankingLinkRepository rankingLinkRepository;
    private final SkillRepository skillRepository;

    public RankService(RankingLinkRepository rankingLinkRepository, SkillRepository skillRepository) {
        this.rankingLinkRepository = rankingLinkRepository;
        this.skillRepository = skillRepository;
    }

    public void proceedResults(ScrimDto scrimDto,
                               ScrimResultsDto scrimResultsDto,
                               List<TabInfoDto> tabInfoDtos) {

        List<UUID> listOfPlayers = tabInfoDtos.stream().map(TabInfoDto::getPlayerId).toList();
        if (listOfPlayers.isEmpty()) {
            throw new IllegalArgumentException("No players found");
        }

        for (UUID playerId : listOfPlayers) {
            TabInfoDto tabInfo = tabInfoDtos.stream()
                    .filter(t -> t.getPlayerId().equals(playerId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("TabInfo not found for player: " + playerId));

            Skill skill = skillRepository.findByPlayerIdAndGameId(playerId, scrimDto.getGameId());

            boolean isWin = tabInfo.getTeamId().equals(scrimResultsDto.getWinnerId());
            double kdr = (double) tabInfo.getKills() / Math.max(tabInfo.getDeaths(), 1);

            if (skill == null) {
                skill = new Skill();
                skill.setPlayerId(playerId);
                skill.setGameId(scrimDto.getGameId());
                skill.setMatchesPlayed(1);
                skill.setWonGames(isWin ? 1 : 0);
                skill.setLostGames(isWin ? 0 : 1);
                skill.setKillDeathRatio(kdr);
                skill.setRR(0); // calibration
                skill.setSkillLevel(0);
            } else {
                skill.setMatchesPlayed(skill.getMatchesPlayed() + 1);
                if (isWin) {
                    skill.setWonGames(skill.getWonGames() + 1);
                } else {
                    skill.setLostGames(skill.getLostGames() + 1);
                }
                skill.setKillDeathRatio(kdr);

                if (skill.getMatchesPlayed() > 10) {
                    double multiplier = getMultiplier(skill.getMatchesPlayed());
                    int delta = (int) Math.round(K * multiplier);

                    if (isWin) {
                        skill.setRR(skill.getRR() + delta);
                    } else {
                        skill.setRR(Math.max(0, skill.getRR() - delta));
                    }
                }
            }

            skillRepository.save(skill);
        }
    }

    public double getMultiplier(int matchesPlayed) {
        return Math.max(1.0, 3.8 - 0.05 * matchesPlayed); // Уменьшается на 0.05 за матч, минимум 1.0
    }


    public List<Rank> getRanks(UUID id) {
        List<RankingLink> playerRankingLinks = rankingLinkRepository.findByPlayerProfileId(id);
        if (playerRankingLinks.isEmpty()) {
            return List.of();
        }
        return playerRankingLinks.stream().map(RankingLink::getRank).collect(Collectors.toList());
    }
}
