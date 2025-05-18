package kz.saya.finals.teamservice.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "team_members")
@Getter
@Setter
public class TeamMember extends MappedSuperClass {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIgnore
    private Team team;

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @Column(name = "is_active")
    private boolean isActive;

    @Transient
    private UUID userId; // Не сохраняем в базе
}

