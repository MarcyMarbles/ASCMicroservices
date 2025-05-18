package kz.saya.finals.common.DTOs.Scrim;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class ScrimEndedDTO {
    private UUID scrimId;
    private Map<UUID, List<PlayerResult>> playerResult; // Team to player result
    private UUID winnerId; // Winner team
    private UUID mvpId; // MVP player

    @Data
    public static class PlayerResult {
        private UUID playerId;
        private int kills;
        private int deaths;
        private int assists;
        private int score;
    }
}
