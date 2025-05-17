package kz.saya.finals.mainservice.Entities;

import jakarta.persistence.*;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import kz.saya.sbasecore.Entity.FileDescriptor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievements")
public class Achievement extends MappedSuperClass {
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int progress;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "icon_id")
    private FileDescriptor icon;
}