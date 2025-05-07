package kz.saya.finals.scrimservice.Service;

import kz.saya.finals.common.DTOs.ScrimDto;
import kz.saya.finals.common.DTOs.ScrimRequestDto;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import kz.saya.finals.scrimservice.Entities.Scrim;
import kz.saya.finals.scrimservice.Entities.ScrimResults;
import kz.saya.finals.scrimservice.Entities.TabInfo;
import kz.saya.finals.scrimservice.Repositories.ScrimRepository;
import kz.saya.finals.scrimservice.Repositories.ScrimResultsRepository;
import kz.saya.finals.scrimservice.Repositories.TabInfoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ScrimService {
    private final ScrimRepository scrimRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public ScrimService(ScrimRepository scrimRepository,
                        UserServiceClient userServiceClient) {
        this.scrimRepository = scrimRepository;
        this.userServiceClient = userServiceClient;
    }

    public ScrimDto createScrim(ScrimRequestDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        String login = auth.getName();
        UserDTO user = userServiceClient.getByLogin(login);

        Scrim scrim = new Scrim();
        scrim.setName(dto.getName());
        scrim.setGameId(dto.getGameId());
        scrim.setScrimType(dto.getScrimType());
        scrim.setPrivate(dto.isPrivate());
        scrim.setCreatorId(user.getId());
        scrim.setCreatorName(user.getUsername());

        Scrim saved = scrimRepository.save(scrim);
        return mapToDto(saved);
    }

    public List<ScrimDto> getAllScrims() {
        return scrimRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ScrimDto getScrimById(UUID id) {
        Scrim scrim = scrimRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found"));
        return mapToDto(scrim);
    }

    public ScrimDto updateScrim(UUID id, ScrimRequestDto dto) {
        Scrim existing = scrimRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth != null ? auth.getName() : null;
        UserDTO user = userServiceClient.getByLogin(login);
        if (!existing.getCreatorId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner");
        }

        existing.setName(dto.getName());
        existing.setGameId(dto.getGameId());
        existing.setScrimType(dto.getScrimType());
        existing.setPrivate(dto.isPrivate());

        Scrim saved = scrimRepository.save(existing);
        return mapToDto(saved);
    }

    public void deleteScrim(UUID id) {
        Scrim existing = scrimRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth != null ? auth.getName() : null;
        UserDTO user = userServiceClient.getByLogin(login);
        if (!existing.getCreatorId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner");
        }

        scrimRepository.delete(existing);
    }

    private ScrimDto mapToDto(Scrim scrim) {
        return new ScrimDto()
                .setId(scrim.getId())
                .setName(scrim.getName())
                .setGameId(scrim.getGameId())
                .setGameName(scrim.getGameName())
                .setPlayerList(scrim.getPlayerList())
                .setTeamList(scrim.getTeamList())
                .setScrimType(scrim.getScrimType())
                .setCreatorId(scrim.getCreatorId())
                .setCreatorName(scrim.getCreatorName());
    }
}