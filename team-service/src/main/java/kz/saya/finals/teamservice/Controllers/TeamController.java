package kz.saya.finals.teamservice.Controllers;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.finals.common.DTOs.TeamCreateDto;
import kz.saya.finals.common.DTOs.TeamDto;
import kz.saya.finals.common.DTOs.TeamUpdateDto;
import kz.saya.finals.feigns.Clients.GamerProfileServiceClient;
import kz.saya.finals.teamservice.Entities.Team;
import kz.saya.finals.teamservice.Mapper.TeamMapper;
import kz.saya.finals.teamservice.Services.TeamService;
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

    private GamerProfileDto getCurrentGamerProfile() {
        String login = getUserLoginFromAuth();
        if (login == null) throw new RuntimeException("Unauthorized");
        GamerProfileDto dto = gamerProfileServiceClient.getProfileByLogin(login);
        if (dto == null) throw new RuntimeException("GamerProfile not found");
        return dto;
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
        if (teamCreateDto == null) return ResponseEntity.badRequest().build();
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        teamCreateDto.setCreatorId(gamerProfileDto.getId());
        Team team = teamService.createTeam(teamCreateDto);
        return ResponseEntity.status(201).body(team);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Team> update(@PathVariable UUID id, @RequestBody TeamUpdateDto teamUpdateDto) {
        if (teamUpdateDto == null) return ResponseEntity.badRequest().build();

        Team team = teamService.getTeamById(id);
        if (team == null) return ResponseEntity.notFound().build();

        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        if (!gamerProfileDto.getId().equals(team.getCreatorId())) return ResponseEntity.status(401).build();

        teamService.updateTeam(id, teamUpdateDto);
        return ResponseEntity.ok(team);
    }

    @PostMapping("/join/{teamId}")
    public ResponseEntity<?> requestJoin(@PathVariable UUID teamId) {
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        teamService.requestToJoinTeam(teamId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{teamId}/{playerId}")
    public ResponseEntity<?> acceptJoinRequest(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        teamService.acceptJoinRequest(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{teamId}/{playerId}")
    public ResponseEntity<?> rejectJoinRequest(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        teamService.rejectJoinRequest(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/kick/{teamId}/{playerId}")
    public ResponseEntity<?> kickPlayer(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        teamService.kickPlayer(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bench/{teamId}/{playerId}")
    public ResponseEntity<?> benchPlayer(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        teamService.benchPlayer(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unbench/{teamId}/{playerId}")
    public ResponseEntity<?> unbenchPlayer(@PathVariable UUID teamId, @PathVariable UUID playerId) {
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        teamService.unbenchPlayer(teamId, playerId, gamerProfileDto.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<TeamDto>> getTeamsByCreator(@PathVariable UUID creatorId) {
        List<Team> teams = teamService.getTeamsByCreator(creatorId);
        teams.get(0).getMembers().forEach(member -> {
            member.setUserId(gamerProfileServiceClient.getProfileById(member.getPlayerId()).getUserId());
        });
        return ResponseEntity.ok(TeamMapper.toDtoList(teams));
    }

    @GetMapping("/member/{playerId}")
    public ResponseEntity<List<TeamDto>> getTeamsByMember(@PathVariable UUID playerId) {
        List<Team> teams = teamService.getTeamsByMember(playerId);
        // Скорее всего у человека будет 1 команда на профиль, не буду менять, буду брать .get(0)
        teams.get(0).getMembers().forEach(member -> {
            member.setUserId(gamerProfileServiceClient.getProfileById(member.getPlayerId()).getUserId());
            // TODO: Либо добавить поле UserID в TeamMember, либо оставить так, не разрушая архитектуру
        });
        return ResponseEntity.ok(TeamMapper.toDtoList(teams));
    }
}
