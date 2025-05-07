package kz.saya.finals.mainservice.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "games") // множественное число в таблице — good practice
public class Game extends MappedSuperClass {
    @Column(nullable = false, unique = true)
    private String name; // Например, "Valorant", "CS2"

    @Column(columnDefinition = "TEXT")
    private String description; // Описание с Steam, IGDB и т.д.

    private String genre; // "MOBA", "Shooter", "Battle Royale"

    private String imageUrl; // логотип или постер игры

    private String officialSite; // если хочешь

    private boolean scrimEnabled = false; // Если игра в которой нельзя создавать кастомки, то будто бы и нельзя

}
