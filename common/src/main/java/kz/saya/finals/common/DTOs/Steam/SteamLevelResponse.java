package kz.saya.finals.common.DTOs.Steam;

import lombok.Data;

import java.util.List;

@Data
public class SteamLevelResponse {
    private Response response;
    
    @Data
    static class Response {
        private List<Badges> badges;
        private int player_xp;
        private int player_level;
        private int player_xp_needed_current_level;
        private int player_xp_needed_to_level_up;
    }

    @Data
    static class Badges {
        private int badgeid;
        private int level;
        private int completion_time;
        private int xp;
        private int scarcity;

    }
}
