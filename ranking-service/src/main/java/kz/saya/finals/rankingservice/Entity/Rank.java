package kz.saya.finals.rankingservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kz.saya.sbasecore.Entity.FileDescriptor;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "rank")
@Getter
@Setter
/**
 * Больше будет задействован как справочник рангов
 * */
public class Rank extends MappedSuperClass {
    private String name;
    private String description;
    @ManyToOne
    private FileDescriptor icon;
    private UUID gameId;
    private int minSkillLevel; // Минимальный уровень навыка для получения ранга
}
