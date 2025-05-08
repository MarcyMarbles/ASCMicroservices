package kz.saya.finals.feigns.Clients;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.sbasesecurity.Feigns.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "main-service", path = "/api/gamer-profiles", configuration = FeignAuthConfig.class)
public interface GamerProfileServiceClient {

    @GetMapping("/by-login")
    GamerProfileDto getProfileByLogin(@RequestParam("login") String login);

    @GetMapping("/by-id")
    GamerProfileDto getProfileById(@RequestParam("id") UUID id);
}
