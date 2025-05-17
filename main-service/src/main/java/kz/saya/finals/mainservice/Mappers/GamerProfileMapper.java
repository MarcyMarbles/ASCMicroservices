package kz.saya.finals.mainservice.Mappers;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.finals.mainservice.Entities.GamerProfile;

public class GamerProfileMapper {
    public static GamerProfileDto toDTO(GamerProfile profile) {
        if (profile == null) return null;

        return new GamerProfileDto()
                .setId(profile.getId())
                .setNickname(profile.getNickname())
                .setDiscordName(profile.getDiscordName())
                .setDescription(profile.getDescription())
                .setRegion(profile.getRegion().name())
                .setUserId(profile.getUserId());
    }

    public static GamerProfile toEntity(GamerProfileDto dto) {
        if (dto == null) return null;

        GamerProfile profile = new GamerProfile();
        profile.setId(dto.getId());
        profile.setNickname(dto.getNickname());
        profile.setDiscordName(dto.getDiscordName());
        profile.setDescription(dto.getDescription());
        profile.setUserId(dto.getUserId());
        return profile;
    }
}
