package kz.saya.finals.mainservice.Mappers;

import kz.saya.finals.common.DTOs.GameDTO;
import kz.saya.finals.mainservice.Entities.Game;
import kz.saya.sbasecore.Entity.FileDescriptor;

public class GameMapper {

    public static GameDTO toDTO(Game game) {
        if (game == null) return null;
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setDescription(game.getDescription());
        if (game.getImage() != null) {
            dto.setImageData(game.getImage().getFileData());
        }
        dto.setGenre(game.getGenre());
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
        if (dto.getImageData() != null) {
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setFileData(dto.getImageData());
            game.setImage(fileDescriptor);
        }
        game.setOfficialSite(dto.getOfficialSite());
        return game;
    }
}
