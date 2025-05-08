package kz.saya.finals.common.DTOs;

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
public class CreateTournamentDto implements Serializable {
    private String name;
    private String description;
    private UUID gameId;
    private UUID creatorId; // ID создателя турнира
}
