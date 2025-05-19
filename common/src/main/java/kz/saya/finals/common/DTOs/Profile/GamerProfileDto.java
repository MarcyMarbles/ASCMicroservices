package kz.saya.finals.common.DTOs.Profile;

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
public class GamerProfileDto implements Serializable {
    private UUID id;
    private String nickname;
    private String steamId;
    private String discordName;
    private String description;
    private String region;
    private UUID userId;
}