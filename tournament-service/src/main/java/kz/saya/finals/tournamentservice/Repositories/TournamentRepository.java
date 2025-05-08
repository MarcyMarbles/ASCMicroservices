package kz.saya.finals.tournamentservice.Repositories;

import kz.saya.finals.tournamentservice.Entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
}