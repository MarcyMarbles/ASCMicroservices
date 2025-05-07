package kz.saya.finals.scrimservice.Controllers;

import kz.saya.finals.common.DTOs.ScrimDto;
import kz.saya.finals.common.DTOs.ScrimRequestDto;
import kz.saya.finals.scrimservice.Service.ScrimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<ScrimDto> list() {
        return scrimService.getAllScrims();
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