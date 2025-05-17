package kz.saya.finals.common.DTOs.Steam;

import lombok.Data;

import java.util.List;

@Data
public class SteamUserAchievementsResponse {
    private Response playerstats;

    @Data
    public static class Response {
        private String steamId;
        private String gameName;
        private List<Achievements> achievements;
    }

    @Data
    public static class Achievements {
        private String apiname;
        private int achieved;
        private long unlocktime;
        private String name;
        private String description;
    }
}


