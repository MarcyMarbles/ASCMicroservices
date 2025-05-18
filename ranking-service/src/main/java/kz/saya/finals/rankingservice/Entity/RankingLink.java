package kz.saya.finals.rankingservice.Entity;

import jakarta.persistence.Access;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "rank_to_player_profile_link")
@Getter
@Setter
@Accessors(chain = true)
public class RankingLink extends MappedSuperClass {
    private UUID playerProfileId;
    @ManyToOne
    private Rank rank;
    // Простая MtM связь для связи игроков и ранга
}
