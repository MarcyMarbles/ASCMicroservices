package kz.saya.finals.feigns.Clients;

import kz.saya.finals.common.DTOs.GameDTO;
import kz.saya.sbasesecurity.Feigns.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "main-service", contextId = "gameServiceClient", path = "/api/games", configuration = FeignAuthConfig.class)
public interface GameServiceClient {

    @GetMapping("/public/{id}")
    GameDTO getGameById(@PathVariable UUID id);


}
