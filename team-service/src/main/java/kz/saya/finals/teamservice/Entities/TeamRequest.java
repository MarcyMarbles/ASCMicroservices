package kz.saya.finals.teamservice.Entities;

import jakarta.persistence.*;
import kz.saya.finals.teamservice.Enums.RequestStatus;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "team_requests")
@Getter
@Setter
public class TeamRequest extends MappedSuperClass {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "player_id", nullable = false)
    private UUID playerId; // Игрок, который отправляет запрос

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING; // Статус запроса
}
