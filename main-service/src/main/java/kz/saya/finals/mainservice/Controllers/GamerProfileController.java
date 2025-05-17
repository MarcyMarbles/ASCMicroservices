package kz.saya.finals.mainservice.Controllers;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import kz.saya.finals.mainservice.Entities.Achievement;
import kz.saya.finals.mainservice.Entities.GamerProfile;
import kz.saya.finals.mainservice.Mappers.GamerProfileMapper;
import kz.saya.finals.mainservice.Services.GamerProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gamer-profiles")
public class GamerProfileController {

    private final UserServiceClient userServiceClient;
    private final GamerProfileService gamerProfileService;

    public GamerProfileController(UserServiceClient userServiceClient, GamerProfileService gamerProfileService) {
        this.userServiceClient = userServiceClient;
        this.gamerProfileService = gamerProfileService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody GamerProfileDto gamerProfileDto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String login = auth.getName();
        UserDTO user = userServiceClient.getByLogin(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (gamerProfileService.getByUserId(user.getId()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profile already exists");
        }
        gamerProfileDto.setUserId(user.getId());
        try {
            GamerProfile createdGamerProfile = gamerProfileService.createGamerProfile(gamerProfileDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(GamerProfileMapper.toDTO(createdGamerProfile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/by-login")
    public ResponseEntity<GamerProfileDto> getProfileByLogin(@RequestParam String login) {
        UserDTO user = userServiceClient.getByLogin(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        GamerProfile profile = gamerProfileService.getByUserId(user.getId());
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(GamerProfileMapper.toDTO(profile));
    }

    @GetMapping("/by-id")
    public ResponseEntity<GamerProfileDto> getProfileById(@RequestParam UUID id) {
        GamerProfile profile = gamerProfileService.getById(id);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(GamerProfileMapper.toDTO(profile));
    }

    @GetMapping("/achievements")
    public ResponseEntity<List<Achievement>> getAchievements() {
        GamerProfile profile = getCurrentUserProfile();
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        List<Achievement> achievements = profile.getAchievements();
        if (achievements == null || achievements.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(achievements);
    }

    @PostMapping("/steam/link")
    public ResponseEntity<?> linkSteamAccount(@RequestParam String steamId) {
        GamerProfile profile = getCurrentUserProfile();
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            gamerProfileService.linkSteamAccount(profile.getId(), steamId);
            return ResponseEntity.ok("Steam account linked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/steam/unlink")
    public ResponseEntity<?> unlinkSteamAccount() {
        GamerProfile profile = getCurrentUserProfile();
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            gamerProfileService.unlinkSteamAccount(profile.getId());
            return ResponseEntity.ok("Steam account unlinked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/steam/friends/scan")
    public ResponseEntity<?> fetchSteamFriends() {
        GamerProfile profile = getCurrentUserProfile();
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            gamerProfileService.scanFriendList(profile.getId());
            return ResponseEntity.ok("Steam friends fetched successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private GamerProfile getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        String login = auth.getName();
        UserDTO user = userServiceClient.getByLogin(login);
        if (user == null) {
            return null;
        }
        return gamerProfileService.getByUserId(user.getId());
    }
}