package kz.saya.finals.feigns.Clients;

import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimEndedDTO;
import kz.saya.finals.common.DTOs.Scrim.ScrimResultsDto;
import kz.saya.finals.common.DTOs.Scrim.TabInfoDto;
import kz.saya.sbasesecurity.Feigns.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "scrim-service", path = "/api/scrims", configuration = FeignAuthConfig.class)
public interface ScrimServiceClient {
    @GetMapping("/{id}")
    ScrimDto get(@PathVariable UUID id);

    @GetMapping("/{id}/scrim-results")
    ScrimResultsDto getResults(@PathVariable UUID id);

    @GetMapping("/{id}/tab-info")
    List<TabInfoDto> getTabInfo(@PathVariable UUID id);

    @PostMapping("/end")
    Object endScrim(@RequestBody ScrimEndedDTO scrimEndedDTO);

}
