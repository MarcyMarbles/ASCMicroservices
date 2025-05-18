package kz.saya.finals.matchmakingservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Matchmaking extends MappedSuperClass {
    private String region; // region of the matchmaking
    @OneToMany
    private List<Matchmaker> players; // list of players in the matchmaking
    private UUID gameId;
    private String gameMode; // game mode of the matchmaking -> DM, TDM, Competitive, etc.
}
