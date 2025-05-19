package kz.saya.finals.scrimservice.Repositories;

import kz.saya.finals.scrimservice.Entities.Scrim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScrimRepository extends JpaRepository<Scrim, UUID> {

    Optional<Scrim> findByIdAndStartedIsFalse(UUID scrimId);
}