package kz.saya.finals.matchmakingservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Matchmaker extends MappedSuperClass {
    private UUID playerProfileId;
    private int skillLevel;
    private UUID gameId;
    private String role; // for some games like Dota 2, League of Legends, etc. the role of the player is important
    private Instant joinTime; // time when the player joined the matchmaking
    private String region;
}
