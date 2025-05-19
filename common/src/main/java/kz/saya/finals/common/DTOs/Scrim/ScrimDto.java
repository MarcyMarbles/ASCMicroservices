package kz.saya.finals.common.DTOs.Scrim;

import kz.saya.finals.common.Enums.ScrimType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ScrimDto implements Serializable {
    private UUID id;
    private String name;
    private UUID gameId;
    private String gameName;
    private List<UUID> playerList;
    private List<UUID> teamList;
    private ScrimType scrimType;
    private UUID creatorId;
    private String creatorName;
    private boolean started;
    private boolean ranked;
}