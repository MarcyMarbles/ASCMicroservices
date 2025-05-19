package kz.saya.finals.scrimservice.Controllers;

import kz.saya.finals.common.DTOs.Scrim.ScrimDto;
import kz.saya.finals.common.DTOs.Scrim.ScrimEndedDTO;
import kz.saya.finals.common.DTOs.Scrim.ScrimRequestDto;
import kz.saya.finals.scrimservice.Service.ScrimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * REST‑controller that exposes CRUD operations and match‑flow actions for {@code Scrim} entities.
 * <p>
 * ✦  Conventional RESTful resource structure:
 * <ul>
 *     <li><strong>POST   /api/scrims</strong>                – create lobby</li>
 *     <li><strong>GET    /api/scrims</strong>                – list lobbies</li>
 *     <li><strong>GET    /api/scrims/{id}</strong>           – lobby details</li>
 *     <li><strong>PUT    /api/scrims/{id}</strong>           – edit open lobby</li>
 *     <li><strong>DELETE /api/scrims/{id}</strong>           – delete lobby</li>
 *     <li><strong>POST   /api/scrims/{id}/join</strong>     – join lobby</li>
 *     <li><strong>POST   /api/scrims/{id}/leave</strong>    – leave lobby</li>
 *     <li><strong>POST   /api/scrims/{id}/start</strong>    – lock & start match</li>
 *     <li><strong>POST   /api/scrims/{id}/results</strong>  – submit final stats</li>
 * </ul>
 * p>
 * ✦  The service layer remains the single source of business‑logic; the controller merely converts HTTP to method calls.
 */
@RestController
@RequestMapping("/api/scrims")
@RequiredArgsConstructor
public class ScrimController {

    private final ScrimService scrimService;

    /**
     * Create a new scrim lobby.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScrimDto create(@RequestBody @Validated ScrimRequestDto request) {
        return scrimService.createScrim(request);
    }

    /**
     * Return the list of all scrims (both open and running).
     */
    @GetMapping
    public List<ScrimDto> list() {
        return scrimService.getAllScrims();
    }

    /**
     * Fetch details of a single scrim lobby.
     */
    @GetMapping("/{id}")
    public ScrimDto get(@PathVariable UUID id) {
        return scrimService.getScrimById(id);
    }

    /**
     * Edit lobby settings (allowed only while <code>started == false</code>).
     */
    @PutMapping("/{id}")
    public ScrimDto update(@PathVariable UUID id,
                           @RequestBody @Validated ScrimRequestDto request) {
        return scrimService.updateScrim(id, request);
    }

    /**
     * Delete a lobby (creator only).
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        scrimService.deleteScrim(id);
    }


    /**
     * Join an open lobby.
     */
    @PostMapping("/{scrimId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ScrimDto join(@PathVariable UUID scrimId) {
        return scrimService.joinScrim(scrimId);
    }

    /**
     * Leave an open lobby.
     */
    @PostMapping("/{scrimId}/leave")
    public ScrimDto leave(@PathVariable UUID scrimId) {
        return scrimService.leaveScrim(scrimId);
    }

    /**
     * Lock the lobby and start the game (creator only).
     */
    @PostMapping("/{scrimId}/start")
    public ScrimDto start(@PathVariable UUID scrimId) {
        return scrimService.startScrim(scrimId);
    }

    /**
     * Persist the final result sheet when the game ends.
     */
    @PostMapping("/{scrimId}/results")
    public ResponseEntity<Void> submitResults(@PathVariable UUID scrimId,
                                              @RequestBody @Validated ScrimEndedDTO endedDTO) {
        if (!scrimId.equals(endedDTO.getScrimId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (endedDTO.getNonTeamPlayerResult() != null && !endedDTO.getNonTeamPlayerResult().isEmpty()) {
            scrimService.endPlayerScrim(endedDTO);
        } else if (endedDTO.getTeamPlayerResult() != null && !endedDTO.getTeamPlayerResult().isEmpty()) {
            scrimService.endTeamScrim(endedDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
}
