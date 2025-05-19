package kz.saya.finals.feigns.Clients;

import kz.saya.finals.common.DTOs.Scrim.ScrimToRankDTO;
import kz.saya.sbasesecurity.Feigns.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "ranking-service", path = "/api/ranking", configuration = FeignAuthConfig.class)
public interface RankingServiceClient {
    @PostMapping("/game/results")
    void proceedResults(@RequestBody ScrimToRankDTO scrim);
}
