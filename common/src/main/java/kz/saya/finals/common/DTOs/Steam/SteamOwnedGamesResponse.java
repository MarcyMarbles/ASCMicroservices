package kz.saya.finals.common.DTOs.Steam;

import lombok.Data;

import java.util.List;

@Data
public class SteamOwnedGamesResponse {
    private Response response;

    @Data
    public static class Response {
        private int game_count;
        private List<Game> games;
    }

    @Data
    public static class Game {
        private int appid;
        private String name;
        private int playtime_forever;
        private int playtime_2weeks;
        private String img_icon_url;
        private int[] content_descriptorids;
    }
}

