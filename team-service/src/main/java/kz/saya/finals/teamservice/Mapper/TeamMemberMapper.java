package kz.saya.finals.teamservice.Mapper;

import kz.saya.finals.common.DTOs.Teams.TeamMemberDto;
import kz.saya.finals.teamservice.Entities.TeamMember;

import java.util.List;

public class TeamMemberMapper {
    public static TeamMemberDto toDto(TeamMember teamMember) {
        if (teamMember == null) return null;
        return new TeamMemberDto()
                .setId(teamMember.getId())
                .setPlayerId(teamMember.getPlayerId())
                .setActive(teamMember.isActive())
                .setUserId(teamMember.getUserId());
    }

    public static TeamMember toEntity(TeamMemberDto teamMemberDto) {
        if (teamMemberDto == null) return null;
        TeamMember teamMember = new TeamMember();
        teamMember.setId(teamMemberDto.getId());
        teamMember.setPlayerId(teamMemberDto.getPlayerId());
        teamMember.setActive(teamMemberDto.isActive());
        return teamMember;
    }

    public static List<TeamMemberDto> toDtoList(List<TeamMember> teamMembers) {
        if (teamMembers == null) return null;
        return teamMembers.stream()
                .map(TeamMemberMapper::toDto)
                .toList();
    }

    public static List<TeamMember> toEntityList(List<TeamMemberDto> teamMemberDtos) {
        if (teamMemberDtos == null) return null;
        return teamMemberDtos.stream()
                .map(TeamMemberMapper::toEntity)
                .toList();
    }
}
