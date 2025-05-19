package kz.saya.finals.tournamentservice.Services;

import kz.saya.finals.common.DTOs.Tournament.CreateTournamentDto;
import kz.saya.finals.common.DTOs.Profile.GamerProfileDto;
import kz.saya.finals.feigns.Clients.GamerProfileServiceClient;
import kz.saya.finals.tournamentservice.Entities.Tournament;
import kz.saya.finals.tournamentservice.Entities.TournamentParticipant;
import kz.saya.finals.tournamentservice.Repositories.TournamentParticipantRepository;
import kz.saya.finals.tournamentservice.Repositories.TournamentRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final GamerProfileServiceClient gamerProfileServiceClient;


    public TournamentService(TournamentRepository tournamentRepository, TournamentParticipantRepository tournamentParticipantRepository, GamerProfileServiceClient gamerProfileServiceClient) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.gamerProfileServiceClient = gamerProfileServiceClient;
    }


    public void createTournament(CreateTournamentDto createTournamentDto) {
        if (createTournamentDto == null) {
            throw new IllegalArgumentException("Tournament data cannot be null");
        }
        if (createTournamentDto.getName() == null || createTournamentDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be null or empty");
        }
        Tournament tournament = new Tournament();
        tournament.setName(createTournamentDto.getName());
        tournament.setDescription(createTournamentDto.getDescription());
        tournament.setGameId(createTournamentDto.getGameId());
        tournament.setCreatorId(createTournamentDto.getCreatorId());
        tournamentRepository.save(tournament);
    }

    public void deleteTournament(UUID tournamentId, UUID requesterId) {
        if (tournamentId == null) {
            throw new IllegalArgumentException("Tournament ID cannot be null");
        }
        if (requesterId == null) {
            throw new IllegalArgumentException("Requester ID cannot be null");
        }
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        if (!tournament.getCreatorId().equals(requesterId)) {
            throw new IllegalArgumentException("Only the creator can delete the tournament");
        }
        tournament.setDeleted_ts(OffsetDateTime.now());
        tournamentRepository.save(tournament);
    }

    public void joinTournament(UUID tournamentId, UUID requesterId) {
        if (tournamentId == null || requesterId == null) {
            throw new IllegalArgumentException("Tournament ID and Requester ID cannot be null");
        }
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        if (tournament.getDeleted_ts() != null) {
            throw new IllegalArgumentException("Tournament is deleted");
        }
        if (tournament.getParticipants().stream()
                .anyMatch(participant -> participant.getGamerProfileId().equals(requesterId))) {
            throw new IllegalArgumentException("You are already a participant in this tournament");
        }
        TournamentParticipant participant = new TournamentParticipant();
        participant.setTournament(tournament);
        participant.setGamerProfileId(requesterId);
        GamerProfileDto gamerProfile = gamerProfileServiceClient.getProfileById(requesterId);
        if (gamerProfile == null) {
            throw new IllegalArgumentException("Gamer profile not found");
        }
        participant.setGamerNickname(gamerProfile.getNickname());
        tournamentParticipantRepository.save(participant);
    }

    public void leaveTournament(UUID tournamentId, UUID requesterId) {
        if (tournamentId == null || requesterId == null) {
            throw new IllegalArgumentException("Tournament ID and Requester ID cannot be null");
        }
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        if (tournament.getDeleted_ts() != null) {
            throw new IllegalArgumentException("Tournament is deleted");
        }
        TournamentParticipant participant = tournamentParticipantRepository.findByTournamentIdAndGamerProfileId(tournamentId, requesterId);
        if (participant == null) {
            throw new IllegalArgumentException("You are not a participant in this tournament");
        }
        if (tournament.getStart_date_ts().isAfter(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Tournament has already started");
        }
        tournamentParticipantRepository.delete(participant);
    }

    public Tournament getTournamentById(UUID tournamentId) {
        if (tournamentId == null) {
            throw new IllegalArgumentException("Tournament ID cannot be null");
        }
        return tournamentRepository.findById(tournamentId).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public List<TournamentParticipant> getTournamentParticipants(UUID tournamentId) {
        if (tournamentId == null) {
            throw new IllegalArgumentException("Tournament ID cannot be null");
        }
        return tournamentParticipantRepository.findAllByTournamentId(tournamentId);
    }

}
