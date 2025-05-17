package kz.saya.finals.mainservice.Services;

import kz.saya.finals.common.DTOs.Steam.SteamOwnedGamesResponse;
import kz.saya.finals.common.DTOs.Steam.SteamUserData;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class SteamService {

    private String apiKey = "823C5B903DF12E4358FF3CB3CD5D38AA";

    private RestTemplate restTemplate = new RestTemplate();

    public SteamUserData getUserData(String steamId) {
        String url = String.format(
                "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s",
                apiKey, steamId
        );
        return restTemplate.getForObject(url, SteamUserData.class);
    }


    public String getSteamId(String steamURL) {
        if (steamURL.matches("https?://steamcommunity\\.com/profiles/(\\d{17})/?")) {
            return steamURL.replaceAll("https?://steamcommunity\\.com/profiles/(\\d{17})/?", "$1");
        }

        if (steamURL.matches("https?://steamcommunity\\.com/id/[a-zA-Z0-9_-]+/?")) {
            String vanityId = steamURL.replaceAll("https?://steamcommunity\\.com/id/([a-zA-Z0-9_-]+)/?", "$1");

            String resolveUrl = String.format(
                    "https://api.steampowered.com/ISteamUser/ResolveVanityURL/v1/?key=%s&vanityurl=%s",
                    apiKey, vanityId
            );

            try {
                @SuppressWarnings("unchecked")
                var response = (Map<String, Object>) restTemplate.getForObject(resolveUrl, Map.class);

                if (response != null && response.containsKey("response")) {
                    Map<String, Object> innerResponse = (Map<String, Object>) response.get("response");
                    if (innerResponse.containsKey("steamid")) {
                        return (String) innerResponse.get("steamid");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public SteamOwnedGamesResponse getUserGames(String steamId) {
        String url = String.format(
                "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=%s&include_appinfo=1",
                apiKey, steamId
        );
        SteamOwnedGamesResponse response = restTemplate.getForObject(url, SteamOwnedGamesResponse.class);
        if (response == null || response.getResponse() == null) {
            return null;
        }
        response.getResponse().setGames(
                response.getResponse().getGames().stream()
                        .filter(game -> game.getPlaytime_forever() > 0)
                        .toList()
        );
        response.getResponse().setGame_count(
                response.getResponse().getGames().size()
        );
        return response;
    }

    public Object getUserAchievements(String steamId, String appId) {
        String url = String.format(
                "https://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v1/?key=%s&steamid=%s&appid=%s&include_played_free_games=1",
                apiKey, steamId, appId
        );
        return restTemplate.getForObject(url, Object.class);
    }

    public Object getUserBadges(String steamId) {
        String url = String.format(
                "https://api.steampowered.com/IPlayerService/GetBadges/v1/?key=%s&steamid=%s",
                apiKey, steamId
        );
        return restTemplate.getForObject(url, Object.class);
    }

    public Object getUserLevel(String steamId) {
        String url = String.format(
                "https://api.steampowered.com/IPlayerService/GetBadges/v1/?key=%s&steamid=%s",
                apiKey, steamId
        );
        return restTemplate.getForObject(url, Object.class);
    }
}
