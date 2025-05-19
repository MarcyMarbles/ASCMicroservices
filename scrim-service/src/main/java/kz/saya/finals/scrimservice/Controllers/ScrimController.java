package kz.saya.finals.scrimservice.Controllers;

import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimEndedDTO;
import kz.saya.finals.common.DTOs.Scrim.ScrimRequestDto;
import kz.saya.finals.scrimservice.Entities.Scrim;
import kz.saya.finals.scrimservice.Service.ScrimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scrims")
public class ScrimController {
    private final ScrimService scrimService;

    @Autowired
    public ScrimController(ScrimService scrimService) {
        this.scrimService = scrimService;
    }

    @PostMapping
    public ResponseEntity<ScrimDto> create(@RequestBody ScrimRequestDto request) {
        ScrimDto created = scrimService.createScrim(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/join")
    public ResponseEntity<ScrimDto> join(@RequestParam UUID scrimId) {
        ScrimDto joined = scrimService.joinScrim(scrimId);
        if (joined == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found");
        }
        return new ResponseEntity<>(joined, HttpStatus.CREATED);
    }

    @PostMapping("/leave")
    public ResponseEntity<ScrimDto> leave(@RequestParam UUID scrimId) {
        ScrimDto left = scrimService.leaveScrim(scrimId);
        if (left == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found");
        }
        return new ResponseEntity<>(left, HttpStatus.CREATED);
    }

    @PostMapping("/start")
    public ResponseEntity<ScrimDto> start(@RequestParam UUID scrimId) {
        ScrimDto started = scrimService.startScrim(scrimId);
        if (started == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scrim not found");
        }
        return new ResponseEntity<>(started, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ScrimDto> list() {
        return scrimService.getAllScrims();
    }

    @PostMapping("/end")
    public ResponseEntity<?> resultGame(@RequestBody ScrimEndedDTO scrimEndedDTO) {
        boolean exists = scrimService.isExists(scrimEndedDTO.getScrimId());
        if (!exists) {
            return ResponseEntity.notFound().build();
        }
        scrimService.endScrim(scrimEndedDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScrimDto> get(@PathVariable UUID id) {
        ScrimDto dto = scrimService.getScrimById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScrimDto> update(@PathVariable UUID id,
                                           @RequestBody ScrimRequestDto request) {
        ScrimDto dto = scrimService.updateScrim(id, request);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scrimService.deleteScrim(id);
        return ResponseEntity.noContent().build();
    }
}