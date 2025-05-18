package kz.saya.finals.scrimservice.Service;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimEndedDTO;
import kz.saya.finals.common.DTOs.Scrim.ScrimRequestDto;
import kz.saya.finals.common.DTOs.Scrim.TabInfoDto;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.finals.feigns.Clients.GamerProfileServiceClient;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import kz.saya.finals.scrimservice.Entities.Scrim;
import kz.saya.finals.scrimservice.Entities.ScrimResults;
import kz.saya.finals.scrimservice.Entities.TabInfo;
import kz.saya.finals.scrimservice.Repositories.ScrimRepository;
import kz.saya.finals.scrimservice.Repositories.ScrimResultsRepository;
import kz.saya.finals.scrimservice.Repositories.TabInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScrimService {
    private final ScrimRepository scrimRepository;
    private final UserServiceClient userServiceClient;
    private final GamerProfileServiceClient gamerProfileServiceClient;
    private final TabInfoRepository tabInfoRepository;
    private final ScrimResultsRepository scrimResultsRepository;

    @Autowired
    public ScrimService(ScrimRepository scrimRepository,
                        UserServiceClient userServiceClient,
                        GamerProfileServiceClient gamerProfileServiceClient, TabInfoRepository tabInfoRepository, ScrimResultsRepository scrimResultsRepository) {
        this.scrimRepository = scrimRepository;
        this.userServiceClient = userServiceClient;
        this.gamerProfileServiceClient = gamerProfileServiceClient;
        this.tabInfoRepository = tabInfoRepository;
        this.scrimResultsRepository = scrimResultsRepository;
    }

    public boolean isExists(UUID id) {
        return scrimRepository.existsById(id);
    }

    public ScrimDto createScrim(ScrimRequestDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        String login = auth.getName();
        GamerProfileDto gamerProfile = gamerProfileServiceClient.getProfileByLogin(login);
        if (gamerProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gamer profile not found");
        }

        Scrim scrim = new Scrim();
        scrim.setName(dto.getName());
        scrim.setGameId(dto.getGameId());
        scrim.setScrimType(dto.getScrimType());
        scrim.setPrivate(dto.isPrivate());
        scrim.setCreatorId(gamerProfile.getId());
        scrim.setCreatorName(gamerProfile.getNickname());

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

    public void endScrim(ScrimEndedDTO scrimEndedDTO) {
        Scrim scrim = scrimRepository.findById(scrimEndedDTO.getScrimId()).orElse(null);
        if (scrim == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found");
        }
        int kills = 0;
        int deaths = 0;
        int assists = 0;
        int lKills = 0;
        int lDeaths = 0;
        int lAssists = 0;
        UUID mvpId = null;
        for (UUID teamId : scrimEndedDTO.getPlayerResult().keySet()) {
            if (!scrim.getTeamList().contains(teamId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Team is not in the scrim");
            }
            List<ScrimEndedDTO.PlayerResult> playerResults = scrimEndedDTO.getPlayerResult().get(teamId);
            for (ScrimEndedDTO.PlayerResult playerResult : playerResults) {
                if (!scrim.getPlayerList().contains(playerResult.getPlayerId())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Player is not in the scrim");
                }
                if (teamId.equals(scrimEndedDTO.getWinnerId())) {
                    kills += playerResult.getKills();
                    deaths += playerResult.getDeaths();
                    assists += playerResult.getAssists();
                } else {
                    lKills += playerResult.getKills();
                    lDeaths += playerResult.getDeaths();
                    lAssists += playerResult.getAssists();
                }
                TabInfo tabInfoDto = new TabInfo();
                tabInfoDto.setPlayerId(playerResult.getPlayerId());
                tabInfoDto.setKills(playerResult.getKills());
                tabInfoDto.setAssists(playerResult.getAssists());
                tabInfoDto.setDeaths(playerResult.getDeaths());
                tabInfoDto.setScore(playerResult.getScore());
                tabInfoRepository.save(tabInfoDto);
            }
            mvpId = getMvp(playerResults);
        }
        ScrimResults scrimResults = new ScrimResults();
        scrimResults.setScrim(scrim);
        scrimResults.setWinnerId(scrimEndedDTO.getWinnerId());
        scrimResults.setMvpId(mvpId);
        scrimResults.setKills(kills);
        scrimResults.setDeaths(deaths);
        scrimResults.setAssists(assists);
        scrimResults.setLKills(lKills);
        scrimResults.setLDeaths(lDeaths);
        scrimResults.setLAssists(lAssists);
        scrimResultsRepository.save(scrimResults);
    }

    private UUID getMvp(List<ScrimEndedDTO.PlayerResult> playerResults) {
        return playerResults.stream()
                .max(Comparator.comparingInt(ScrimEndedDTO.PlayerResult::getKills))
                .map(ScrimEndedDTO.PlayerResult::getPlayerId)
                .orElse(null);
    }
}