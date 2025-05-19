package kz.saya.finals.common.DTOs.Teams;

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
public class TeamMemberDto implements Serializable {
    private UUID id;
    private UUID playerId;
    private boolean isActive;
    private UUID userId; // Для уведомлений, так как они идут на юзера, а не на игрока
}