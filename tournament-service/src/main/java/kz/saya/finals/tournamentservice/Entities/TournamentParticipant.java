package kz.saya.finals.tournamentservice.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tournament_participants")
@Getter
@Setter
public class TournamentParticipant extends MappedSuperClass {
    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
    private UUID gamerProfileId;
    private String gamerNickname; // Чтобы лишний раз не обращаться к сервису профилей
    private UUID teamId; // Если участник в команде, то это поле не null
    private String teamName; // Чтобы лишний раз не обращаться к сервису команд
}
