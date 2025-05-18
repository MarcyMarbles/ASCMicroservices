package kz.saya.finals.scrimservice.Service;

import kz.saya.finals.scrimservice.Entities.ScrimResults;
import kz.saya.finals.scrimservice.Repositories.ScrimResultsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ScrimResultsService {

    private final ScrimResultsRepository resultsRepository;

    @Autowired
    public ScrimResultsService(ScrimResultsRepository resultsRepository) {
        this.resultsRepository = resultsRepository;
    }

    public ScrimResults createResults(ScrimResults results) {
        return resultsRepository.save(results);
    }

    public List<ScrimResults> getByScrim(UUID scrimId) {
        return resultsRepository.findByScrimId(scrimId);
    }

    public ScrimResults getByScrimId(UUID scrimId) {
        List<ScrimResults> results = resultsRepository.findByScrimId(scrimId);
        return results.isEmpty() ? null : results.get(0);
    }

    public ScrimResults updateResults(UUID id, ScrimResults results) {
        ScrimResults existing = resultsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Results not found"));
        BeanUtils.copyProperties(results, existing, "id");
        return resultsRepository.save(existing);
    }

    public void deleteResults(UUID id) {
        resultsRepository.deleteById(id);
    }
}
