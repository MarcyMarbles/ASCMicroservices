package kz.saya.finals.common.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AchievementDto implements Serializable {
    private UUID id;
    private String name;
    private String description;
    private int progress;
    private byte[] iconFileData;
    private UUID gameId;
}