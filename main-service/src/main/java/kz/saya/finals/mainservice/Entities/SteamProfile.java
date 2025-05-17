package kz.saya.finals.mainservice.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class SteamProfile extends MappedSuperClass {
    private String steamId;
    private String steamName;
    private String avatarUrl;// Full avatar url
    private String profileUrl;

    @JsonIgnore
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gamer_profile_id", unique = true)
    private GamerProfile gamerProfile;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "steam_profile_known_friends",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<SteamProfile> knownFriends;

}
