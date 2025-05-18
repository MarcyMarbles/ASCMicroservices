package kz.saya.finals.common.DTOs;

import kz.saya.finals.common.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TeamDto implements Serializable {
    private UUID id;
    private OffsetDateTime created_ts;
    private OffsetDateTime updated_ts;
    private OffsetDateTime deleted_ts;
    private OffsetDateTime start_date_ts;
    private OffsetDateTime end_date_ts;
    private String name;
    private String tag;
    private String description;
    private String country;
    private UUID creatorId;
    private UUID captainId;
    private Status status = Status.ACTIVE;
    private List<TeamMemberDto> members = new ArrayList<>();

}