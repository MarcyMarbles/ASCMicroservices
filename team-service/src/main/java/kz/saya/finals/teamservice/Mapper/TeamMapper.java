package kz.saya.finals.teamservice.Mapper;

import kz.saya.finals.common.DTOs.Teams.TeamDto;
import kz.saya.finals.teamservice.Entities.Team;

import java.util.List;

public class TeamMapper {
    public static TeamDto toDto(Team team) {
        if (team == null) return null;
        return new TeamDto()
                .setId(team.getId())
                .setCreated_ts(team.getCreated_ts())
                .setUpdated_ts(team.getUpdated_ts())
                .setDeleted_ts(team.getDeleted_ts())
                .setStart_date_ts(team.getStart_date_ts())
                .setEnd_date_ts(team.getEnd_date_ts())
                .setName(team.getName())
                .setTag(team.getTag())
                .setDescription(team.getDescription())
                .setCountry(team.getCountry())
                .setCreatorId(team.getCreatorId())
                .setCaptainId(team.getCaptainId())
                .setStatus(team.getStatus())
                .setMembers(TeamMemberMapper.toDtoList(team.getMembers()));
    }

    public static List<TeamDto> toDtoList(List<Team> teams) {
        if (teams == null) return null;
        return teams.stream()
                .map(TeamMapper::toDto)
                .toList();
    }

    public static Team toEntity(TeamDto teamDto) {
        if (teamDto == null) return null;
        Team team = new Team();
        team.setId(teamDto.getId());
        team.setCreated_ts(teamDto.getCreated_ts());
        team.setUpdated_ts(teamDto.getUpdated_ts());
        team.setDeleted_ts(teamDto.getDeleted_ts());
        team.setStart_date_ts(teamDto.getStart_date_ts());
        team.setEnd_date_ts(teamDto.getEnd_date_ts());
        team.setName(teamDto.getName());
        team.setTag(teamDto.getTag());
        team.setDescription(teamDto.getDescription());
        team.setCountry(teamDto.getCountry());
        team.setCreatorId(teamDto.getCreatorId());
        team.setCaptainId(teamDto.getCaptainId());
        team.setStatus(teamDto.getStatus());
        team.setMembers(TeamMemberMapper.toEntityList(teamDto.getMembers()));
        return team;
    }

    public static List<Team> toEntityList(List<TeamDto> teamDtos) {
        if (teamDtos == null) return null;
        return teamDtos.stream()
                .map(TeamMapper::toEntity)
                .toList();
    }
}
