package kz.saya.finals.scrimservice.Repositories;

import kz.saya.finals.scrimservice.Entities.ScrimResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScrimResultsRepository extends JpaRepository<ScrimResults, UUID> {
    List<ScrimResults> findByScrimId(UUID scrimId);
}