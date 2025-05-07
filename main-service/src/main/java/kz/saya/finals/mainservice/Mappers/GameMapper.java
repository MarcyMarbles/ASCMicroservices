package kz.saya.finals.mainservice.Mappers;

import kz.saya.finals.common.DTOs.GameDTO;
import kz.saya.finals.mainservice.Entities.Game;

public class GameMapper {

    public static GameDTO toDTO(Game game) {
        if (game == null) return null;
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setDescription(game.getDescription());
        dto.setGenre(game.getGenre());
        dto.setImageUrl(game.getImageUrl());
        dto.setOfficialSite(game.getOfficialSite());
        return dto;
    }

    public static Game toEntity(GameDTO dto) {
        if (dto == null) return null;
        Game game = new Game();
        game.setId(dto.getId());
        game.setName(dto.getName());
        game.setDescription(dto.getDescription());
        game.setGenre(dto.getGenre());
        game.setImageUrl(dto.getImageUrl());
        game.setOfficialSite(dto.getOfficialSite());
        return game;
    }
}
