package kz.saya.finals.mainservice.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.saya.sbasecore.Entity.FileDescriptor;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class GamerProfile extends MappedSuperClass {
    private String nickname;
    private String discordName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private FileDescriptor avatar;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "background_id")
    private FileDescriptor background;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Region region;

    private UUID userId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gamer_achievements",
            joinColumns = @JoinColumn(name = "gamer_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id")
    )
    @JsonIgnore
    private List<Achievement> achievements;

    @OneToOne(mappedBy = "gamerProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private SteamProfile steamProfile;

}
