package kz.saya.finals.scrimservice.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "scrim_results")
@Getter
@Setter
public class ScrimResults extends MappedSuperClass {
    @ManyToOne
    @JoinColumn(name = "scrim_id", nullable = false)
    private Scrim scrim;
    private UUID winnerId; // ID команды-победителя
    private UUID mvpId; // Человек с большим количеством убийств, если есть
    private int kills; // Количество убийств команды-победителя
    private int deaths; // Количество смертей команды-победителя
    private int assists; // Количество ассистов команды-победителя
    private int lKills; // Количество убийств команды-проигравшей
    private int lDeaths; // Количество смертей команды-проигравшей
    private int lAssists; // Количество ассистов команды-проигравшей

}
