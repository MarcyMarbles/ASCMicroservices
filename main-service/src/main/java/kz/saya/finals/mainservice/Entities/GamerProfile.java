package kz.saya.finals.mainservice.Entities;

import jakarta.persistence.Entity;
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
    private FileDescriptor avatar; // Аватар игрока
    private FileDescriptor background; // Фоновое изображение профиля (ПЛАТНО)
    private String description; // Описание профиля
    private String region; // Регион игрока (например, "EU", "NA", "ASIA")
    private UUID userId; // ID пользователя, которому принадлежит профиль
}
