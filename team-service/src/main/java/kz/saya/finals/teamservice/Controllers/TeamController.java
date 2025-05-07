package kz.saya.finals.teamservice.Controllers;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.finals.common.DTOs.TeamCreateDto;
import kz.saya.finals.common.DTOs.TeamUpdateDto;
import kz.saya.finals.feigns.Clients.GamerProfileServiceClient;
import kz.saya.finals.teamservice.Entities.Team;
import kz.saya.finals.teamservice.Services.TeamService;
import kz.saya.sbasecore.Entity.FileDescriptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final GamerProfileServiceClient gamerProfileServiceClient;

    public TeamController(TeamService teamService, GamerProfileServiceClient gamerProfileServiceClient) {
        this.teamService = teamService;
        this.gamerProfileServiceClient = gamerProfileServiceClient;
    }

    private String getUserLoginFromAuth() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public List<Team> getAll() {
        return teamService.getAllTeams();
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Team> getById(@PathVariable UUID id) {
        Team team = teamService.getTeamById(id);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Team> create(@RequestBody TeamCreateDto teamCreateDto) {
        if (teamCreateDto == null) {
            return ResponseEntity.badRequest().build();
        }
        String userLogin = getUserLoginFromAuth();
        if (userLogin == null) {
            return ResponseEntity.status(401).build();
        }
        GamerProfileDto gamerProfileDto = gamerProfileServiceClient.getProfileByLogin(userLogin);
        if (gamerProfileDto == null) {
            return ResponseEntity.status(401).build();
        }
        teamCreateDto.setCreatorId(gamerProfileDto.getId());
        Team team = teamService.createTeam(teamCreateDto);
        return ResponseEntity.status(201).body(team);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Team> update(@PathVariable UUID id, @RequestBody TeamUpdateDto teamUpdateDto) {
        if (teamUpdateDto == null) {
            return ResponseEntity.badRequest().build();
        }
        String userLogin = getUserLoginFromAuth();
        if (userLogin == null) {
            return ResponseEntity.status(401).build();
        }
        Team team = teamService.getTeamById(id);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }
        GamerProfileDto gamerProfileDto = gamerProfileServiceClient.getProfileByLogin(userLogin);
        if (gamerProfileDto == null || !gamerProfileDto.getId().equals(team.getCreatorId())) {
            return ResponseEntity.status(401).build();
        }
        /*if (teamUpdateDto.getName() != null && !teamUpdateDto.getName().isEmpty()) {
            team.setName(teamUpdateDto.getName());
        }
        if (teamUpdateDto.getTag() != null && !teamUpdateDto.getTag().isEmpty()) {
            team.setTag(teamUpdateDto.getTag());
        }
        if (teamUpdateDto.getDescription() != null && !teamUpdateDto.getDescription().isEmpty()) {
            team.setDescription(teamUpdateDto.getDescription());
        }
        if (teamUpdateDto.getLogo() != null) {
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setFileData(teamUpdateDto.getLogo());
            fileDescriptor.setLabel(teamUpdateDto.getName());
            team.setLogo(fileDescriptor);
        }
        if (teamUpdateDto.getCountry() != null) {
            team.setCountry(teamUpdateDto.getCountry());
        }*/
        teamService.updateTeam(id, teamUpdateDto);
        return ResponseEntity.ok(team);
    }

    @PostMapping("/join/{teamId}")
    public ResponseEntity<?> requestJoin(@PathVariable UUID teamId) {
        String userLogin = getUserLoginFromAuth();
        if (userLogin == null) {
            return ResponseEntity.status(401).build();
        }
        GamerProfileDto gamerProfileDto = gamerProfileServiceClient.getProfileByLogin(userLogin);
        if (gamerProfileDto == null) {
            return ResponseEntity.status(401).build();
        }
        teamService.requestToJoinTeam(teamId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{teamId}/{playerId}")
    public ResponseEntity<?> acceptJoinRequest(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        String userLogin = getUserLoginFromAuth();
        if (userLogin == null) {
            return ResponseEntity.status(401).build();
        }
        GamerProfileDto gamerProfileDto = gamerProfileServiceClient.getProfileByLogin(userLogin);
        if (gamerProfileDto == null) {
            return ResponseEntity.status(401).build();
        }
        teamService.acceptJoinRequest(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{teamId}/{playerId}")
    public ResponseEntity<?> rejectJoinRequest(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        String userLogin = getUserLoginFromAuth();
        if (userLogin == null) {
            return ResponseEntity.status(401).build();
        }
        GamerProfileDto gamerProfileDto = gamerProfileServiceClient.getProfileByLogin(userLogin);
        if (gamerProfileDto == null) {
            return ResponseEntity.status(401).build();
        }
        teamService.rejectJoinRequest(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/kick/{teamId}/{playerId}")
    public ResponseEntity<?> kickPlayer(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        String userLogin = getUserLoginFromAuth();
        if (userLogin == null) {
            return ResponseEntity.status(401).build();
        }
        GamerProfileDto gamerProfileDto = gamerProfileServiceClient.getProfileByLogin(userLogin);
        if (gamerProfileDto == null) {
            return ResponseEntity.status(401).build();
        }
        teamService.kickPlayer(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bench/{teamId}/{playerId}")
    public ResponseEntity<?> benchPlayer(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        String userLogin = getUserLoginFromAuth();
        if (userLogin == null) {
            return ResponseEntity.status(401).build();
        }
        GamerProfileDto gamerProfileDto = gamerProfileServiceClient.getProfileByLogin(userLogin);
        if (gamerProfileDto == null) {
            return ResponseEntity.status(401).build();
        }
        teamService.benchPlayer(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unbench/{teamId}/{playerId}")
    public ResponseEntity<?> unbenchPlayer(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        String userLogin = getUserLoginFromAuth();
        if (userLogin == null) {
            return ResponseEntity.status(401).build();
        }
        GamerProfileDto gamerProfileDto = gamerProfileServiceClient.getProfileByLogin(userLogin);
        if (gamerProfileDto == null) {
            return ResponseEntity.status(401).build();
        }
        teamService.unbenchPlayer(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<Team>> getTeamsByCreator(@PathVariable UUID creatorId) {
        List<Team> teams = teamService.getTeamsByCreator(creatorId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/member/{playerId}")
    public ResponseEntity<List<Team>> getTeamsByMember(@PathVariable UUID playerId) {
        List<Team> teams = teamService.getTeamsByMember(playerId);
        return ResponseEntity.ok(teams);
    }

    private GamerProfileDto getCurrentGamerProfile() {
        String login = getUserLoginFromAuth();
        if (login == null) throw new RuntimeException("Unauthorized");
        GamerProfileDto dto = gamerProfileServiceClient.getProfileByLogin(login);
        if (dto == null) throw new RuntimeException("GamerProfile not found");
        return dto;
    }


}
