package kz.saya.finals.scrimservice.Mapper;

import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.scrimservice.Entities.Scrim;

import java.util.List;

public class ScrimMapper {
    public static ScrimDto toDto(Scrim scrim) {
        return new ScrimDto()
                .setId(scrim.getId())
                .setName(scrim.getName())
                .setGameId(scrim.getGameId())
                .setScrimType(scrim.getScrimType())
                .setCreatorId(scrim.getCreatorId())
                .setCreatorName(scrim.getCreatorName())
                .setPlayerList(scrim.getPlayerList())
                .setTeamList(scrim.getTeamList());
    }

    public static Scrim toEntity(ScrimDto scrimDto) {
        Scrim scrim = new Scrim();
        scrim.setId(scrimDto.getId());
        scrim.setName(scrimDto.getName());
        scrim.setGameId(scrimDto.getGameId());
        scrim.setScrimType(scrimDto.getScrimType());
        scrim.setCreatorId(scrimDto.getCreatorId());
        scrim.setCreatorName(scrimDto.getCreatorName());
        scrim.setPlayerList(scrimDto.getPlayerList());
        scrim.setTeamList(scrimDto.getTeamList());
        return scrim;
    }

    public static List<ScrimDto> toListDto(List<Scrim> scrims) {
        return scrims.stream()
                .map(ScrimMapper::toDto)
                .toList();
    }

    public static List<Scrim> toListEntity(List<ScrimDto> scrimDtos) {
        return scrimDtos.stream()
                .map(ScrimMapper::toEntity)
                .toList();
    }

}
