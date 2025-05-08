package kz.saya.finals.mainservice.Services;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.finals.mainservice.Entities.GamerProfile;
import kz.saya.finals.mainservice.Mappers.GamerProfileMapper;
import kz.saya.finals.mainservice.Repositories.GamerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GamerProfileService {
    @Autowired
    private GamerProfileRepository gamerProfileRepository;

    public List<GamerProfile> getAllGamerProfiles() {
        return gamerProfileRepository.findAll();
    }

    public GamerProfile createGamerProfile(
            GamerProfileDto gamerProfileDto
    ) {
        GamerProfile gamerProfile = GamerProfileMapper.toEntity(gamerProfileDto);
        return gamerProfileRepository.save(gamerProfile);
    }

    public GamerProfile getByUserId(UUID userId) {
        return gamerProfileRepository.findGamerProfileByUserId(userId);
    }

    public GamerProfile getById(UUID id) {
        return gamerProfileRepository.findById(id).orElse(null);
    }
}
