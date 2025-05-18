package kz.saya.finals.scrimservice.Controllers;

import kz.saya.finals.common.DTOs.Scrim.ScrimResultsDto;
import kz.saya.finals.scrimservice.Entities.ScrimResults;
import kz.saya.finals.scrimservice.Mapper.ScrimResultMapper;
import kz.saya.finals.scrimservice.Service.ScrimResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/scrims/{scrimId}/results")
public class ScrimResultsController {

    private final ScrimResultsService resultsService;

    @Autowired
    public ScrimResultsController(ScrimResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @PostMapping
    public ResponseEntity<ScrimResults> create(@RequestBody ScrimResults results) {
        return new ResponseEntity<>(resultsService.createResults(results), HttpStatus.CREATED);
    }

    @GetMapping
    public ScrimResultsDto get(@PathVariable UUID scrimId) {
        return ScrimResultMapper.toDto(resultsService.getByScrimId(scrimId));
    }

    @PutMapping("/{id}")
    public ScrimResults update(@PathVariable UUID id, @RequestBody ScrimResults results) {
        return resultsService.updateResults(id, results);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        resultsService.deleteResults(id);
    }
}