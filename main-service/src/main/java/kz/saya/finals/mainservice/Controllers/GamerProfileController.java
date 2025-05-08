package kz.saya.finals.mainservice.Controllers;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import kz.saya.finals.mainservice.Entities.GamerProfile;
import kz.saya.finals.mainservice.Mappers.GamerProfileMapper;
import kz.saya.finals.mainservice.Services.GamerProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

}
