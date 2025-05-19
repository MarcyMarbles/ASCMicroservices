package kz.saya.finals.common.DTOs.Tournament;

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
public class TournamentParticipantDto implements Serializable {
    private UUID id;
    private OffsetDateTime created_ts;
    private OffsetDateTime updated_ts;
    private OffsetDateTime deleted_ts;
    private OffsetDateTime start_date_ts;
    private OffsetDateTime end_date_ts;
    private UUID tournamentId;
    private UUID gamerProfileId;
    private String gamerNickname;
    private UUID teamId;
    private String teamName;
}