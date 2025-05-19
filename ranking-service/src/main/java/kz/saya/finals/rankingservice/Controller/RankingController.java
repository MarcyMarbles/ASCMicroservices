package kz.saya.finals.rankingservice.Controller;

import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimResultsDto;
import kz.saya.finals.common.DTOs.Scrim.TabInfoDto;
import kz.saya.finals.feigns.Clients.ScrimServiceClient;
import kz.saya.finals.rankingservice.Service.RankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final ScrimServiceClient scrimServiceClient;
    private final RankService rankService;

    public RankingController(ScrimServiceClient scrimServiceClient, RankService rankService) {
        this.scrimServiceClient = scrimServiceClient;
        this.rankService = rankService;
    }

    @PostMapping("/game/results")
    public ResponseEntity<?> proceedResults(@RequestParam UUID scrimId) {
        ScrimDto scrimDto = scrimServiceClient.get(scrimId);
        if (scrimDto == null) {
            return ResponseEntity.badRequest().body("Scrim not found");
        }
        ScrimResultsDto scrimResultsDto = scrimServiceClient.getResults(scrimId);
        if (scrimResultsDto == null) {
            return ResponseEntity.badRequest().body("Scrim is not ended properly");
        }
        List<TabInfoDto> tabInfoDtos = scrimServiceClient.getTabInfo(scrimId);
        if (tabInfoDtos == null || tabInfoDtos.isEmpty()) {
            return ResponseEntity.badRequest().body("Scrim is not ended properly");
        }
        rankService.proceedResults(scrimDto, scrimResultsDto, tabInfoDtos);
        return ResponseEntity.ok("Results processed successfully");
    }

    @GetMapping
    public ResponseEntity<?> getRanks(){

    }
}
