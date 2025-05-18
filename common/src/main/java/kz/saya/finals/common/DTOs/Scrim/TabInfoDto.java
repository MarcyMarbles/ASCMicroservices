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
public class TabInfoDto implements Serializable {
    private UUID id;
    private UUID scrimId;
    private UUID playerId;
    private UUID teamId;
    private String nickname;
    private String teamName;
    private int kills;
    private int deaths;
    private int assists;
    private int position;
    private int score;
    private int damage;
    private int heal;
    private int revives;
}