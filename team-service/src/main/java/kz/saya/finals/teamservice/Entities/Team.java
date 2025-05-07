package kz.saya.finals.teamservice.Entities;

import jakarta.persistence.*;
import kz.saya.finals.common.Enums.Status;
import kz.saya.sbasecore.Entity.FileDescriptor;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "team")
@Getter
@Setter
public class Team extends MappedSuperClass {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true, nullable = false, length = 10)
    private String tag;

    @Column(length = 1024)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_id")
    private FileDescriptor logo;

    @Column(length = 64)
    private String country;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "captain_id")
    private UUID captainId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> members = new ArrayList<>();
}


