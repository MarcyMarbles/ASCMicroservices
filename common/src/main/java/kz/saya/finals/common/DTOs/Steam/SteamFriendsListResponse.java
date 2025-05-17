package kz.saya.finals.common.DTOs.Steam;

import lombok.Data;

import java.util.List;

@Data
public class SteamFriendsListResponse {
    private Response friendslist;

    @Data
    public static class Response {
        private List<Friend> friends;
    }

    @Data
    public static class Friend {
        // Data given by Steam
        private String steamid;
        private String relationship;
        private int friend_since;
        // ----------------------------------
        // Data will be fetched once again
        private String friendName;
        private String avatarUrl;
        private String avatarMediumUrl;
        private String avatarFullUrl;
        private String profileUrl;
    }
}

