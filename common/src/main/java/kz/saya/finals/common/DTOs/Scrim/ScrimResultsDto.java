package kz.saya.finals.common.DTOs.Scrim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ScrimResultsDto implements Serializable {
    private UUID id;
    private UUID scrimId;
    private UUID winnerId;
    private UUID mvpId;
    private int kills;
    private int deaths;
    private int assists;
    private int lKills;
    private int lDeaths;
    private int lAssists;
}