package kz.saya.finals.common.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TournamentDto implements Serializable {
    private UUID id;
    private OffsetDateTime created_ts;
    private OffsetDateTime updated_ts;
    private OffsetDateTime deleted_ts;
    private OffsetDateTime start_date_ts;
    private OffsetDateTime end_date_ts;
    private String name;
    private String description;
    private String gameName;
    private UUID gameId;
    private Set<TournamentParticipantDto> participants = new HashSet<>();
}

