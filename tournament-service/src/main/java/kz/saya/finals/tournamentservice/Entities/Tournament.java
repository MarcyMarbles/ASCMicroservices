package kz.saya.finals.tournamentservice.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
public class Tournament extends MappedSuperClass {
    private String name;
    private String description;
    private String gameName;
    private UUID gameId;
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TournamentParticipant> participants = new HashSet<>();
    private UUID creatorId; // ID создателя турнира


}
