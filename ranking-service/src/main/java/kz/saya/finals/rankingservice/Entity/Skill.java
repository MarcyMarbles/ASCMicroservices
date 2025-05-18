package kz.saya.finals.rankingservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import kz.saya.finals.rankingservice.EventListeners.SkillEntityListener;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.event.EventListener;

import java.util.UUID;

@Entity
@Table(name = "skill")
@Getter
@Setter
@EntityListeners(SkillEntityListener.class)
public class Skill extends MappedSuperClass {
    private UUID playerId;
    private UUID gameId;
    private int matchesPlayed; // Количество сыгранных матчей
    private int wonGames;
    private int lostGames;
    private int skillLevel; // ++ or -- based on the game result
    private int RR; // Rank Rating, used to calculate the rank of the player
    private double killDeathRatio; // K/D ratio of the player, changed from the game result
}
