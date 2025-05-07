package kz.saya.finals.scrimservice.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kz.saya.sbasecore.Entity.MappedSuperClass;

@Entity
@Table(name = "tab_info")
public class TabInfo extends MappedSuperClass {
    @ManyToOne
    @JoinColumn(name = "scrim_id", nullable = false)
    private Scrim scrim;
    private String nickname; // Никнейм игрока
    private String teamName; // Название команды, для группировки
    private int kills; // Количество убийств
    private int deaths; // Количество смертей
    private int assists; // Количество ассистов
    private int position; // Позиция игрока в матче, сортируем по kills
    private int score; // Счет игрока, сортируем по kills
    private int damage; // Урон игрока, если игра даёт такую информацию
    private int heal; // Лечение игрока, если игра даёт такую информацию
    private int revives; // Количество воскрешений игрока, если игра даёт такую информацию

}
