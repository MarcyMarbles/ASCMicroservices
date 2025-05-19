package kz.saya.finals.mainservice.Services;

import kz.saya.finals.common.DTOs.Profile.GamerProfileDto;
import kz.saya.finals.common.DTOs.Steam.SteamFriendsListResponse;
import kz.saya.finals.common.DTOs.Steam.SteamUserData;
import kz.saya.finals.mainservice.Entities.GamerProfile;
import kz.saya.finals.mainservice.Entities.SteamProfile;
import kz.saya.finals.mainservice.Mappers.GamerProfileMapper;
import kz.saya.finals.mainservice.Repositories.GamerProfileRepository;
import kz.saya.finals.mainservice.Repositories.SteamProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class GamerProfileService {
    @Autowired
    private GamerProfileRepository gamerProfileRepository;
    @Autowired
    private SteamProfileRepository steamProfileRepository;
    @Autowired
    private SteamService steamService;

    public List<GamerProfile> getAllGamerProfiles() {
        return gamerProfileRepository.findAll();
    }

    public GamerProfile createGamerProfile(
            GamerProfileDto gamerProfileDto
    ) {
        GamerProfile gamerProfile = GamerProfileMapper.toEntity(gamerProfileDto);
        return gamerProfileRepository.save(gamerProfile);
    }

    public GamerProfile getByUserId(UUID userId) {
        return gamerProfileRepository.findGamerProfileByUserId(userId);
    }

    public GamerProfile getById(UUID id) {
        return gamerProfileRepository.findById(id).orElse(null);
    }

    public GamerProfile linkSteamAccount(UUID id, String steamId) {
        if (steamId == null || steamId.isEmpty()) {
            throw new IllegalArgumentException("Steam ID cannot be null or empty");
        }
        if (steamId.contains("http")) {
            throw new IllegalArgumentException("To find your Steam ID, please use /api/gamer-profiles/steam-id");
        }

        GamerProfile gamerProfile = gamerProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gamer profile not found"));

        if (gamerProfile.getSteamProfile() != null) {
            throw new IllegalArgumentException("Steam account already linked");
        }

        SteamProfile existingSteam = steamProfileRepository.findBySteamId(steamId);
        if (existingSteam != null) {
            throw new IllegalArgumentException("This Steam account is already linked to another profile");
        }

        SteamUserData steamUserData = steamService.getUserData(steamId);
        if (steamUserData == null || steamUserData.getResponse() == null ||
                steamUserData.getResponse().getPlayers() == null || steamUserData.getResponse().getPlayers().isEmpty()) {
            throw new IllegalArgumentException("Steam ID not found");
        }

        SteamUserData.Player player = steamUserData.getResponse().getPlayers().get(0);

        SteamProfile steamProfile = buildSteamProfile(steamId, gamerProfile, player);

        gamerProfile.setSteamProfile(steamProfile);

        gamerProfileRepository.save(gamerProfile);

        CompletableFuture<Void> parseNeededData; // Я хочу сделать так чтобы ассинхроно запрашивались данные
        // Например время проведения в игре, и если у человека есть игры где он наиграл больше 1000 часов
        // Выдавать ему системную ачивку


        return gamerProfile;
    }

    public GamerProfile unlinkSteamAccount(UUID id) {
        GamerProfile gamerProfile = gamerProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gamer profile not found"));

        if (gamerProfile.getSteamProfile() == null) {
            throw new IllegalArgumentException("Steam account not linked");
        }

        SteamProfile steamProfile = gamerProfile.getSteamProfile();
        gamerProfile.setSteamProfile(null);
        steamProfileRepository.delete(steamProfile);

        return gamerProfile;
    }

    /**
     * Мы не можем манипулировать хоть как либо на список друзей
     * Мы можем только получать информацию из Steam насчёт его списка друзей
     */
    public GamerProfile scanFriendList(UUID id) {
        GamerProfile gamerProfile = gamerProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gamer profile not found"));

        SteamProfile steamProfile = gamerProfile.getSteamProfile();
        if (steamProfile == null) {
            throw new IllegalArgumentException("Steam account not linked");
        }

        SteamFriendsListResponse response = steamService.getUserFriends(steamProfile.getSteamId(), false);
        // includeDetails = false -> Мы берём только ID друзей, нам не нужны их данные
        if (response == null || response.getFriendslist() == null || response.getFriendslist().getFriends() == null) {
            return gamerProfile; // просто нет друзей
        }

        List<SteamProfile> friendsInSystem = new ArrayList<>();
        for (SteamFriendsListResponse.Friend friend : response.getFriendslist().getFriends()) {
            SteamProfile existing = steamProfileRepository.findBySteamId(friend.getSteamid());
            if (existing != null) {
                friendsInSystem.add(existing);
            }
        }
        if (friendsInSystem.isEmpty()) {
            throw new IllegalArgumentException("No friends from the Steam found in the system");
        }

        steamProfile.setKnownFriends(friendsInSystem);
        steamProfileRepository.save(steamProfile);

        return gamerProfile;
    }


    private SteamProfile buildSteamProfile(String steamId, GamerProfile gamerProfile, SteamUserData.Player player) {
        SteamProfile profile = new SteamProfile();
        profile.setSteamId(steamId);
        profile.setSteamName(player.getPersonaname());
        profile.setAvatarUrl(player.getAvatarfull());
        profile.setProfileUrl(player.getProfileurl());
        profile.setGamerProfile(gamerProfile);
        return profile;
    }

}
