package kz.saya.finals.teamservice.Services;

import kz.saya.finals.common.DTOs.Teams.TeamCreateDto;
import kz.saya.finals.common.DTOs.Teams.TeamUpdateDto;
import kz.saya.finals.teamservice.Entities.Team;
import kz.saya.finals.teamservice.Entities.TeamMember;
import kz.saya.finals.teamservice.Entities.TeamRequest;
import kz.saya.finals.teamservice.Enums.RequestStatus;
import kz.saya.finals.teamservice.Repositories.TeamRepository;
import kz.saya.finals.teamservice.Repositories.TeamRequestRepository;
import kz.saya.sbasecore.Entity.FileDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamRequestRepository teamRequestRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(UUID id) {
        return teamRepository.findById(id).orElse(null);
    }

    public Team createTeam(TeamCreateDto teamCreateDto) {
        if (teamCreateDto == null) {
            throw new IllegalArgumentException("TeamCreateDto cannot be null");
        }
        Team team = new Team();
        team.setName(teamCreateDto.getName());
        team.setTag(teamCreateDto.getTag());
        team.setDescription(teamCreateDto.getDescription());
        team.setCreatorId(teamCreateDto.getCreatorId());
        return teamRepository.save(team);
    }

    public Team updateTeam(UUID id, TeamUpdateDto teamUpdateDto) {
        Team team = getTeamById(id);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        team.setName(teamUpdateDto.getName());
        team.setDescription(teamUpdateDto.getDescription());
        team.setTag(teamUpdateDto.getTag());
        team.setCountry(teamUpdateDto.getCountry());
        if (teamUpdateDto.getLogo() != null) {
            FileDescriptor logo = new FileDescriptor();
            logo.setLabel("teamLogo");
            logo.setFileData(teamUpdateDto.getLogo());
            logo.setMimeType("image/png");
            logo.setExtension("png");
            logo.setSize(teamUpdateDto.getLogo().length);
            team.setLogo(logo);
        }
        return teamRepository.save(team);
    }

    public void deleteTeam(UUID id) {
        Team team = getTeamById(id);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        teamRepository.delete(team);
    }

    public void addMember(UUID teamId, UUID playerId) {
        Team team = getTeamById(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        List<UUID> members = team.getMembers().stream().map(TeamMember::getPlayerId).toList();
        if (members.contains(playerId)) {
            throw new IllegalArgumentException("Player is already a member of the team");
        }
        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);
        teamMember.setPlayerId(playerId);
        teamMember.setActive(true);
        team.getMembers().add(teamMember);
        teamRepository.save(team);
    }

    public void benchPlayer(UUID teamId, UUID playerId, UUID creatorId) {
        Team team = getTeamById(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        if (!team.getCreatorId().equals(creatorId)) {
            throw new IllegalArgumentException("Only the creator can bench players");
        }
        List<UUID> members = team.getMembers().stream().map(TeamMember::getPlayerId).toList();
        if (!members.contains(playerId)) {
            throw new IllegalArgumentException("Player is not a member of the team");
        }
        TeamMember teamMember = team.getMembers().stream()
                .filter(member -> member.getPlayerId().equals(playerId))
                .findFirst()
                .orElse(null);
        if (teamMember != null) {
            teamMember.setActive(false);
            teamRepository.save(team);
        }
    }

    public void unbenchPlayer(UUID teamId, UUID playerId, UUID creatorId) {
        Team team = getTeamById(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        if (!team.getCreatorId().equals(creatorId)) {
            throw new IllegalArgumentException("Only the creator can unbench players");
        }
        List<UUID> members = team.getMembers().stream().map(TeamMember::getPlayerId).toList();
        if (!members.contains(playerId)) {
            throw new IllegalArgumentException("Player is not a member of the team");
        }
        TeamMember teamMember = team.getMembers().stream()
                .filter(member -> member.getPlayerId().equals(playerId))
                .findFirst()
                .orElse(null);
        if (teamMember != null) {
            teamMember.setActive(true);
            teamRepository.save(team);
        }
    }

    public void kickPlayer(UUID teamId, UUID playerId, UUID creatorId) {
        Team team = getTeamById(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        if (!team.getCreatorId().equals(creatorId)) {
            throw new IllegalArgumentException("Only the creator can kick players");
        }
        List<UUID> members = team.getMembers().stream().map(TeamMember::getPlayerId).toList();
        if (!members.contains(playerId)) {
            throw new IllegalArgumentException("Player is not a member of the team");
        }
        TeamMember teamMember = team.getMembers().stream()
                .filter(member -> member.getPlayerId().equals(playerId))
                .findFirst()
                .orElse(null);
        if (teamMember != null) {
            team.getMembers().remove(teamMember);
            teamRepository.save(team);
        }
    }

    public List<Team> getTeamsByCreator(UUID creatorId) {
        return teamRepository.findAllByCreatorId(creatorId);
    }

    public List<Team> getTeamsByMember(UUID playerId) {
        return teamRepository.findAll().stream()
                .filter(team -> team.getMembers().stream()
                        .anyMatch(member -> member.getPlayerId().equals(playerId)))
                .collect(Collectors.toList());
    }

    public void requestToJoinTeam(UUID teamId, UUID playerId) {
        Team team = getTeamById(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        if (team.getCreatorId().equals(playerId) && team.getMembers().stream().noneMatch(member -> member.getPlayerId().equals(playerId))) {
            addMember(teamId, playerId);
            return;
        }
        List<UUID> members = team.getMembers().stream().map(TeamMember::getPlayerId).toList();
        if (members.contains(playerId)) {
            throw new IllegalArgumentException("Player is already a member of the team");
        }
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setTeam(team);
        teamRequest.setPlayerId(playerId);
        // status already set to PENDING in the constructor
        teamRequestRepository.save(teamRequest);
    }

    public void acceptJoinRequest(UUID teamId, UUID playerId, UUID creatorId) {
        Team team = getTeamById(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        if (!team.getCreatorId().equals(creatorId)) {
            throw new IllegalArgumentException("Only the creator can accept join requests");
        }
        TeamRequest teamRequest = teamRequestRepository.findByTeamIdAndPlayerId(teamId, playerId);
        if (teamRequest == null) {
            throw new IllegalArgumentException("Join request not found");
        }
        addMember(teamId, playerId);
        teamRequest.setStatus(RequestStatus.ACCEPTED);
        teamRequestRepository.save(teamRequest);
    }

    public void rejectJoinRequest(UUID teamId, UUID playerId, UUID creatorId) {
        Team team = getTeamById(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        if (!team.getCreatorId().equals(creatorId)) {
            throw new IllegalArgumentException("Only the creator can reject join requests");
        }
        TeamRequest teamRequest = teamRequestRepository.findByTeamIdAndPlayerId(teamId, playerId);
        if (teamRequest == null) {
            throw new IllegalArgumentException("Join request not found");
        }
        teamRequest.setStatus(RequestStatus.REJECTED);
        teamRequestRepository.save(teamRequest);
    }
}
