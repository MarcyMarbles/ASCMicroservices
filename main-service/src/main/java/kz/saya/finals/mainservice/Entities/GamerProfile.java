package kz.saya.finals.mainservice.Entities;

import jakarta.persistence.*;
import kz.saya.sbasecore.Entity.FileDescriptor;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class GamerProfile extends MappedSuperClass {
    private String nickname; // Никнейм игрока
    private String steamId; // Steam ID игрока
    private String discordName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private FileDescriptor avatar;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "background_id")
    private FileDescriptor background;

    private String description; // Описание профиля
    private String region; // Регион игрока (например, "EU", "NA", "ASIA")
    private UUID userId; // ID пользователя, которому принадлежит профиль
}
