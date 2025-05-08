package kz.saya.finals.tournamentservice.Repositories;

import kz.saya.finals.tournamentservice.Entities.TournamentParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, UUID> {
    TournamentParticipant findByTournamentIdAndGamerProfileId(UUID tournamentId, UUID requesterId);

    List<TournamentParticipant> findAllByTournamentId(UUID tournamentId);
}