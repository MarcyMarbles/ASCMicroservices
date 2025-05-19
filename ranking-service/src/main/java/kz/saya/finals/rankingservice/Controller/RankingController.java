package kz.saya.finals.rankingservice.Controller;

import kz.saya.finals.common.DTOs.Auth.UserDTO;
import kz.saya.finals.common.DTOs.Profile.GamerProfileDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimResultsDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimToRankDTO;
import kz.saya.finals.common.DTOs.Scrim.TabInfoDto;
import kz.saya.finals.feigns.Clients.GamerProfileServiceClient;
import kz.saya.finals.feigns.Clients.ScrimServiceClient;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import kz.saya.finals.rankingservice.Entity.Rank;
import kz.saya.finals.rankingservice.Entity.RankingLink;
import kz.saya.finals.rankingservice.Service.RankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final ScrimServiceClient scrimServiceClient;
    private final RankService rankService;
    private final GamerProfileServiceClient gamerProfileServiceClient;
    private final UserServiceClient userServiceClient;

    public RankingController(ScrimServiceClient scrimServiceClient, RankService rankService, GamerProfileServiceClient gamerProfileServiceClient, UserServiceClient userServiceClient) {
        this.scrimServiceClient = scrimServiceClient;
        this.rankService = rankService;
        this.gamerProfileServiceClient = gamerProfileServiceClient;
        this.userServiceClient = userServiceClient;
    }

    @PostMapping("/game/results")
    public ResponseEntity<?> proceedResults(@RequestBody ScrimToRankDTO dto) {
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        UserDTO userDTO = getUserFromAuth();
        ScrimDto scrimDto = dto.getScrimDto();
        if (!scrimDto.getCreatorId().equals(gamerProfileDto.getId()) && !userDTO.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to process this scrim’s results");
        }

        ScrimResultsDto scrimResultsDto = dto.getScrimResultsDto();
        if (scrimResultsDto == null) {
            return ResponseEntity.badRequest().body("Scrim is not ended properly");
        }
        List<TabInfoDto> tabInfoDtos = dto.getTabInfoDtos();
        if (tabInfoDtos == null || tabInfoDtos.isEmpty()) {
            return ResponseEntity.badRequest().body("Scrim is not ended properly");
        }
        rankService.proceedResults(scrimDto, scrimResultsDto, tabInfoDtos);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getRanks() {
        GamerProfileDto gamerProfileDto = getCurrentGamerProfile();
        List<Rank> ranks = rankService.getRanks(gamerProfileDto.getId());
        if (ranks == null || ranks.isEmpty()) {
            return ResponseEntity.badRequest().body("No ranks found");
        }
        return ResponseEntity.ok(ranks);
    }

    private String getUserLoginFromAuth() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private UserDTO getUserFromAuth() {
        String login = getUserLoginFromAuth();
        if (login == null) throw new RuntimeException("Unauthorized");
        UserDTO userDTO = userServiceClient.getByLogin(login);
        if (userDTO == null) throw new RuntimeException("User not found");
        return userDTO;
    }

    private GamerProfileDto getCurrentGamerProfile() {
        String login = getUserLoginFromAuth();
        if (login == null) throw new RuntimeException("Unauthorized");
        GamerProfileDto dto = gamerProfileServiceClient.getProfileByLogin(login);
        if (dto == null) throw new RuntimeException("GamerProfile not found");
        return dto;
    }
}
