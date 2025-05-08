package kz.saya.finals.tournamentservice.Controllers;

import kz.saya.finals.common.DTOs.CreateTournamentDto;
import kz.saya.finals.feigns.Clients.GamerProfileServiceClient;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import kz.saya.finals.tournamentservice.Services.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;
    private final UserServiceClient userServiceClient;
    private final GamerProfileServiceClient gamerProfileServiceClient;

    public TournamentController(TournamentService tournamentService, UserServiceClient userServiceClient, GamerProfileServiceClient gamerProfileServiceClient) {
        this.tournamentService = tournamentService;
        this.userServiceClient = userServiceClient;
        this.gamerProfileServiceClient = gamerProfileServiceClient;
    }

    @GetMapping("/{id}")
    public String getTournamentById(@PathVariable UUID id) {
        return tournamentService.getTournamentById(id).toString();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTournament(
            @RequestBody CreateTournamentDto createTournamentDto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Authentication failed");
        }
        String login = auth.getName();
        UUID profileId = gamerProfileServiceClient.getProfileByLogin(login).getId();
        createTournamentDto.setCreatorId(profileId);
        try {
            tournamentService.createTournament(createTournamentDto);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok("Successfully created tournament");
    }

    @PostMapping("/join/{tournamentId}")
    public ResponseEntity<?> joinTournament(
            @PathVariable UUID tournamentId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Authentication failed");
        }
        String login = auth.getName();
        UUID profileId = gamerProfileServiceClient.getProfileByLogin(login).getId();
        try {
            tournamentService.joinTournament(tournamentId, profileId);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok("Successfully joined tournament");
    }


    @PostMapping("/leave/{tournamentId}")
    public ResponseEntity<?> leaveTournament(
            @PathVariable UUID tournamentId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Authentication failed");
        }
        String login = auth.getName();
        UUID profileId = gamerProfileServiceClient.getProfileByLogin(login).getId();
        try {
            tournamentService.leaveTournament(tournamentId, profileId);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok("Successfully left tournament");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }
}
