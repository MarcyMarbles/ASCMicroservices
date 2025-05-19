package kz.saya.finals.scrimservice.Service;

import kz.saya.finals.common.DTOs.Game.GameDTO;
import kz.saya.finals.common.DTOs.Profile.GamerProfileDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimEndedDTO;
import kz.saya.finals.common.DTOs.Scrim.ScrimRequestDto;
import kz.saya.finals.feigns.Clients.GameServiceClient;
import kz.saya.finals.feigns.Clients.GamerProfileServiceClient;
import kz.saya.finals.feigns.Clients.RankingServiceClient;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Refactored service layer for handling scrims.
 * <p>
 * The boolean flag <b>Scrim.started</b> (instead of the old <i>isPrivate</i>) now
 * denotes that the match has begun. While <code>started == false</code> the
 * scrim is visible and players are allowed to join; as soon as the creator
 * starts the match the flag flips to <code>true</code> and the lobby becomes
 * closed for further changes.
 * </p>
 */
@Service
@Transactional
public class ScrimService {

    private final ScrimRepository scrimRepository;
    private final UserServiceClient userServiceClient;
    private final GamerProfileServiceClient gamerProfileServiceClient;
    private final TabInfoRepository tabInfoRepository;
    private final ScrimResultsRepository scrimResultsRepository;
    private final GameServiceClient gameServiceClient;
    private final RankingServiceClient rankingServiceClient;

    @Autowired
    public ScrimService(ScrimRepository scrimRepository,
                        UserServiceClient userServiceClient,
                        GamerProfileServiceClient gamerProfileServiceClient,
                        TabInfoRepository tabInfoRepository,
                        ScrimResultsRepository scrimResultsRepository,
                        GameServiceClient gameServiceClient, RankingServiceClient rankingServiceClient) {
        this.scrimRepository = scrimRepository;
        this.userServiceClient = userServiceClient;
        this.gamerProfileServiceClient = gamerProfileServiceClient;
        this.tabInfoRepository = tabInfoRepository;
        this.scrimResultsRepository = scrimResultsRepository;
        this.gameServiceClient = gameServiceClient;
        this.rankingServiceClient = rankingServiceClient;
    }

    // ---------------------------------------------------------------------
    //  Public API
    // ---------------------------------------------------------------------

    public boolean isExists(UUID id) {
        return scrimRepository.existsById(id);
    }

    public ScrimDto createScrim(ScrimRequestDto dto) {
        GamerProfileDto creator = getCurrentGamerProfile();
        GameDTO game = fetchGame(dto.getGameId());

        Scrim scrim = new Scrim();
        scrim.setName(dto.getName());
        scrim.setGameId(dto.getGameId());
        scrim.setGameName(game.getName());
        scrim.setScrimType(dto.getScrimType());
        scrim.setStarted(false);                 // lobby is open until start()
        scrim.setCreatorId(creator.getId());
        scrim.setCreatorName(creator.getNickname());
        System.out.println(dto);
        scrim.setRanked(dto.isRanked());
        scrim.setPlayerList(new ArrayList<>(List.of(creator.getId())));

        return mapToDto(scrimRepository.save(scrim));
    }

    public ScrimDto joinScrim(UUID scrimId) {
        Scrim scrim = scrimRepository.findByIdAndStartedIsFalse(scrimId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found or already started"));

        GamerProfileDto gamer = getCurrentGamerProfile();
        if (scrim.getPlayerList().contains(gamer.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already in scrim");
        }
        if (scrim.isStarted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scrim already started");
        }
        scrim.getPlayerList().add(gamer.getId());
        return mapToDto(scrimRepository.save(scrim));
    }

    public ScrimDto leaveScrim(UUID scrimId) {
        Scrim scrim = getScrimOrThrow(scrimId);

        GamerProfileDto gamer = getCurrentGamerProfile();
        if (!scrim.getPlayerList().remove(gamer.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not in scrim");
        }
        return mapToDto(scrimRepository.save(scrim));
    }

    public ScrimDto startScrim(UUID scrimId) {
        Scrim scrim = getScrimOrThrow(scrimId);

        GamerProfileDto creator = getCurrentGamerProfile();
        if (!creator.getId().equals(scrim.getCreatorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only creator can start scrim");
        }
        if (scrim.getPlayerList().size() <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough players to start scrim");
        }
        if (scrim.isStarted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scrim already started");
        }

        scrim.setStarted(true);
        return mapToDto(scrimRepository.save(scrim));
    }

    public List<ScrimDto> getAllScrims() {
        return scrimRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ScrimDto getScrimById(UUID id) {
        return mapToDto(getScrimOrThrow(id));
    }

    public ScrimDto updateScrim(UUID id, ScrimRequestDto dto) {
        Scrim scrim = getScrimOrThrow(id);
        GamerProfileDto user = getCurrentGamerProfile();
        if (!scrim.getCreatorId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner");
        }
        if (scrim.isStarted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot edit started scrim");
        }

        scrim.setName(dto.getName());
        scrim.setGameId(dto.getGameId());
        scrim.setScrimType(dto.getScrimType());

        return mapToDto(scrimRepository.save(scrim));
    }

    public void deleteScrim(UUID id) {
        Scrim scrim = getScrimOrThrow(id);
        GamerProfileDto user = getCurrentGamerProfile();
        if (!scrim.getCreatorId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner");
        }
        scrimRepository.delete(scrim);
    }

    public void endScrim(ScrimEndedDTO dto) {
        Scrim scrim = getScrimOrThrow(dto.getScrimId());
        if (!scrim.isStarted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scrim is not running");
        }

        int kills = 0, deaths = 0, assists = 0;
        int lKills = 0, lDeaths = 0, lAssists = 0;
        UUID mvpId = null;

        for (Map.Entry<UUID, List<ScrimEndedDTO.PlayerResult>> entry : dto.getPlayerResult().entrySet()) {
            UUID teamId = entry.getKey();
            List<ScrimEndedDTO.PlayerResult> results = entry.getValue();

            if (!scrim.getTeamList().contains(teamId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Team is not in the scrim");
            }

            for (ScrimEndedDTO.PlayerResult pr : results) {
                if (!scrim.getPlayerList().contains(pr.getPlayerId())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Player is not in the scrim");
                }

                if (teamId.equals(dto.getWinnerId())) {
                    kills += pr.getKills();
                    deaths += pr.getDeaths();
                    assists += pr.getAssists();
                } else {
                    lKills += pr.getKills();
                    lDeaths += pr.getDeaths();
                    lAssists += pr.getAssists();
                }

                TabInfo tab = new TabInfo();
                tab.setPlayerId(pr.getPlayerId());
                tab.setKills(pr.getKills());
                tab.setAssists(pr.getAssists());
                tab.setDeaths(pr.getDeaths());
                tab.setScore(pr.getScore());
                tabInfoRepository.save(tab);
            }
            mvpId = getMvp(results);
        }

        ScrimResults scrimResults = new ScrimResults();
        scrimResults.setScrim(scrim);
        scrimResults.setWinnerId(dto.getWinnerId());
        scrimResults.setMvpId(mvpId);
        scrimResults.setKills(kills);
        scrimResults.setDeaths(deaths);
        scrimResults.setAssists(assists);
        scrimResults.setLKills(lKills);
        scrimResults.setLDeaths(lDeaths);
        scrimResults.setLAssists(lAssists);
        scrimResultsRepository.save(scrimResults);
        scrim.setEnded(true);
        scrim = scrimRepository.save(scrim);
        rankingServiceClient.proceedResults(scrim.getId());
    }


    private Scrim getScrimOrThrow(UUID id) {
        return scrimRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found"));
    }

    private GamerProfileDto getCurrentGamerProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        String login = auth.getName();
        GamerProfileDto profile = gamerProfileServiceClient.getProfileByLogin(login);
        if (profile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gamer profile not found");
        }
        return profile;
    }

    private GameDTO fetchGame(UUID gameId) {
        GameDTO game = gameServiceClient.getGameById(gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        return game;
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
                .setCreatorName(scrim.getCreatorName())
                .setStarted(scrim.isStarted())
                .setRanked(scrim.isRanked());
    }

    private UUID getMvp(List<ScrimEndedDTO.PlayerResult> results) {
        return results.stream()
                .max(Comparator.comparingInt(ScrimEndedDTO.PlayerResult::getKills))
                .map(ScrimEndedDTO.PlayerResult::getPlayerId)
                .orElse(null);
    }

}
