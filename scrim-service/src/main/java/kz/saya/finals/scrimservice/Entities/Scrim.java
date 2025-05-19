package kz.saya.finals.scrimservice.Entities;

import jakarta.persistence.*;
import kz.saya.finals.common.Enums.ScrimType;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Scrim extends MappedSuperClass {
    private String name;
    private UUID gameId;
    private String gameName; // Чтобы не делать лишний запрос к сервису
    @ElementCollection
    @CollectionTable(name = "scrim_players", joinColumns = @JoinColumn(name = "scrim_id"))
    @Column(name = "player_id")
    private List<UUID> playerList;

    @ElementCollection
    @CollectionTable(name = "scrim_teams", joinColumns = @JoinColumn(name = "scrim_id"))
    @Column(name = "team_id")
    private List<UUID> teamList;

    @Enumerated(EnumType.STRING)
    private ScrimType scrimType;
    // StartDate - поле из MappedSuperClass НЕ ТРОГАТЬ
    // EndDate - поле из MappedSuperClass
    private UUID creatorId; // ID создателя скрима
    private String creatorName; // Имя создателя скрима
    private boolean started; // Начался ли скрим


}
