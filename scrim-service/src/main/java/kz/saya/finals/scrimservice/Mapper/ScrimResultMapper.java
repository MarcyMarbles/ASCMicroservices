package kz.saya.finals.scrimservice.Mapper;

import kz.saya.finals.common.DTOs.Scrim.ScrimResultsDto;
import kz.saya.finals.scrimservice.Entities.ScrimResults;

import java.util.List;

public class ScrimResultMapper {
    public static ScrimResultsDto toDto(ScrimResults resultsDto) {
        return new ScrimResultsDto()
                .setId(resultsDto.getId())
                .setScrimId(resultsDto.getScrim().getId())
                .setKills(resultsDto.getKills())
                .setAssists(resultsDto.getAssists())
                .setDeaths(resultsDto.getDeaths())
                .setLKills(resultsDto.getLKills())
                .setLDeaths(resultsDto.getLDeaths())
                .setLAssists(resultsDto.getLAssists())
                .setMvpId(resultsDto.getMvpId());
    }

    public static ScrimResults toEntity(ScrimResultsDto resultsDto) {
        ScrimResults scrimResults = new ScrimResults();
        scrimResults.setId(resultsDto.getId());
        scrimResults.setKills(resultsDto.getKills());
        scrimResults.setAssists(resultsDto.getAssists());
        scrimResults.setDeaths(resultsDto.getDeaths());
        scrimResults.setLKills(resultsDto.getLKills());
        scrimResults.setLDeaths(resultsDto.getLDeaths());
        scrimResults.setLAssists(resultsDto.getLAssists());
        scrimResults.setMvpId(resultsDto.getMvpId());
        return scrimResults;
    }

    public static List<ScrimResultsDto> toDtoList(List<ScrimResults> scrimResults) {
        return scrimResults.stream()
                .map(ScrimResultMapper::toDto)
                .toList();
    }

    public static List<ScrimResults> toEntityList(List<ScrimResultsDto> scrimResultsDtos) {
        return scrimResultsDtos.stream()
                .map(ScrimResultMapper::toEntity)
                .toList();
    }

}
