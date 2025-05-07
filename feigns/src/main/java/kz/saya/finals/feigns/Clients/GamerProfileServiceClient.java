package kz.saya.finals.feigns.Clients;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "main-service", path = "/api/gamer-profiles")
public interface GamerProfileServiceClient {

    @GetMapping("/by-login")
    GamerProfileDto getProfileByLogin(@RequestParam("login") String login);
}
