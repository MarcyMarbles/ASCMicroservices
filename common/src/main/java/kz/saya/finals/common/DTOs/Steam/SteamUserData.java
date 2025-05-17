package kz.saya.finals.common.DTOs.Steam;

import lombok.Data;

import java.util.List;

@Data
public class SteamUserData {
    private Response response;

    @Data
    public static class Response {
        private List<Player> players;
    }

    @Data
    public static class Player {
        private String steamid;
        private int communityvisibilitystate;
        private int profilestate;
        private String personaname;
        private int commentpermission;
        private String profileurl;
        private String avatar;
        private String avatarmedium;
        private String avatarfull;
        private String avatarhash;
        private int personastate;
        private String primaryclanid;
        private int timecreated;
        private int personastateflags;
    }
}
